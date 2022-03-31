package com.jian.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jian.entity.Result;
import com.jian.entity.User;
import com.jian.mapper.Avatar_Mapper;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.HmacSHA512_Util;
import com.jian.untils.JWTUtils;
import com.jian.untils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("JMask/User")
public class User_Controller {
    final
    User_Mapper userMapper;
    final
    Avatar_Mapper avatarMapper;

    public User_Controller(User_Mapper userMapper, Avatar_Mapper avatarMapper) {
        this.userMapper = userMapper;
        this.avatarMapper = avatarMapper;
    }

    /**
     * 检查用户上传的token是否有效
     *
     * @param token 用户上传的token
     * @return 是否有效 boolean
     */
    private DecodedJWT checkToken(String token) {
        if (token == null || "".equals(token)) {
            return null;
        }
        try {
            return JWTUtils.verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("select")
    public List<User> selectUsers() {
//        System.out.println(userMapper.select());
        return userMapper.selectAllUsers();
    }


    /**
     * 通过Token获得用户密码
     *
     * @param token 用户Token
     * @return Result HmacSHA512加密的用户密码
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "getPassWordByToken")
    public Result getPassWordByToken(@RequestParam(value = "token") String token) {
        DecodedJWT verify;
        Map<String, String> data = new HashMap<>();
        if (token == null) {
            data.put("error", "Token为空");
            return Result.getFail().setData(data);
        }
        try {
            verify = JWTUtils.verify(token);
        } catch (Exception e) {
            data.put("error", "Token无效");
            return Result.getFail().setData(data);
        }
        User user = userMapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            data.put("error", "用户无效");
            return Result.getFail().setData(data);
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = timeFormat.parse(user.getCreateDate());
        } catch (ParseException e) {
            e.printStackTrace();
            data.put("error", "服务器错误");
            return Result.getFail().setData(data);
        }
        String createTime = String.valueOf(date.getTime() / 1000); // 时间戳转换日期

        data.put("passWord", Base64Util.decode(user.getPassword()));
        data.put("time", createTime);
        return Result.getSuccess().setData(data);
    }

    /**
     * 更新用户的用户名
     *
     * @param token       用户Token
     * @param newUserName 新的用户名
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "updateUserNameByToken")
    public Result updateUserNameByToken(@RequestParam(value = "token") String token, @RequestParam(value = "newUserName") String newUserName) {
        DecodedJWT verify = checkToken(token);
        Map<String, String> data = new HashMap<>();
        if (verify == null) {
            data.put("error", "Token无效");
            return Result.getFail().setData(data);
        }
        boolean result = userMapper.updateUserName(verify.getClaim("id").asString(), newUserName);
//        System.out.println(result);
        if (result) {
            return Result.getSuccess();
        } else {
            return Result.getFail();
        }
    }


    @RequestMapping("selectByUserID")
    public String selectByUserID(String ID) {
//        System.out.println(userMapper.select());
        User user = userMapper.selectUserByID(ID);
        String result = "";
        try {
            result = Base64Util.encode(JsonUtils.toJson(user));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("selectByUserName")
    public User selectByUserName(String name) {
//        System.out.println(userMapper.select());
        return userMapper.selectUserByName(name);
    }

    /**
     * 更新用户密码
     *
     * @param token       用户token
     * @param newPassWord 新的密码
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "updateUserPassWord")
    public Result updateUserPassWord(@RequestParam(value = "token") String token, @RequestParam(value = "newPassWord") String newPassWord) {
        Map<String, String> data = new HashMap<>();
//        System.out.println(newPassWord);
        DecodedJWT verify = checkToken(token);
        if (verify == null) {
            data.put("error", "Token无效");
            return Result.getFail().setData(data);
        }
        User user = userMapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            data.put("error", "用户信息无效");
            return Result.getFail().setData(data);
        }
        boolean result = userMapper.updatePassWord(user.getId(), newPassWord);
        if (result) {
            return Result.getSuccess();
        } else {
            return Result.getFail();
        }
    }


    @RequestMapping(method = {RequestMethod.POST}, value = "getUserInfoByToken")
    public Result getUserInfoByToken(@RequestParam(value = "token") String token) {
        Map<String, String> data = new HashMap<>();
        DecodedJWT verify = checkToken(token);
        if (verify == null) {
            data.put("error", "Token无效");
            return Result.getFail().setData(data);
        }
        User user = userMapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            data.put("error", "用户信息无效");
            return Result.getFail().setData(data);
        }
        String avatarURL = avatarMapper.selectAvatarByUserID(user.getId());
        if (avatarURL == null || "".equals(avatarURL)) {
            avatarURL = "";
        }
        data.put("id", user.getId().toUpperCase());
        data.put("avatarURL", avatarURL);
        data.put("userName", user.getUserName());
        return Result.getSuccess().setData(data);
    }

    @RequestMapping(method = {RequestMethod.POST}, value = "getUserIdByToken")
    public Result getUserIdByToken(@RequestParam(value = "token") String token) {
        DecodedJWT verify = null;
//        System.out.println(token);
        if (token == null) {
            return Result.getFail();
        }
        try {
            verify = JWTUtils.verify(token);
        } catch (Exception e) {
            return Result.getFail().setData("用户信息无效");
        }
        User user = userMapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            return Result.getFail().setData("用户信息无效");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("id", user.getId().toUpperCase(Locale.ROOT));
        return Result.getSuccess().setData(data);
    }

    @RequestMapping(method = {RequestMethod.POST}, value = "removeAccount")
    public Result removeAccount(@RequestParam(value = "token") String token, @RequestParam(value = "userName") String userName, @RequestParam(value = "passWord") String passWord, @RequestParam(value = "encodePassWord") String encodePassWord) {
        DecodedJWT verify = checkToken(token);
        HashMap<String, String> data = new HashMap<>();
        if (verify == null) {
            data.put("error", "Token无效");
            return Result.getFail().setData(data);
        }
        User user = userMapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            data.put("error", "用户无效");
            return Result.getFail().setData(data);
        }
        if (!(user.getUserName().equals(userName))) {
            data.put("error", "用户信息错误");
            return Result.getFail().setData(data);
        }
        if (!(user.getPassword().equals(encodePassWord))) {
            data.put("error", "用户信息错误");
            return Result.getFail().setData(data);
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = timeFormat.parse(user.getCreateDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.getFail();
        }
        String createTime = String.valueOf(date.getTime() / 1000); // 时间戳转换日期

        String H512PassWord;
        try {
            H512PassWord = Base64Util.encode(HmacSHA512_Util.HmacSHA512(passWord, createTime));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Result.getFail();
        }
        if (!(user.getPassword().equals(H512PassWord))) {
            data.put("error", "用户信息错误");
            return Result.getFail().setData(data);
        }
        boolean result = userMapper.removeAccount(user.getId());
        if (result) {
            return Result.getSuccess();
        }
        return Result.getFail();
    }

    /**
     * @param file 用户上传的头像(图片)
     * @param token 用户token
     * @return
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/uploadAvatar"
    )
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
//        System.out.println("OK");
//        System.out.println(token);
        DecodedJWT verify;
        Map<String, String> data = new HashMap<>();
        if (token == null) {
            return Result.getFail();
        }
        try {
            verify = JWTUtils.verify(token);
        } catch (Exception e) {
            data.put("error", "Token无效");
            return Result.getFail().setData(data);
        }
        User user = userMapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            return Result.getFail().setData("用户无效");
        }

        String fileName = file.getOriginalFilename();
        int index = Objects.requireNonNull(file.getOriginalFilename()).indexOf(".");
        if ("".equals(fileName) || fileName == null || index <= 0) {
            data.put("error", "无文件");
            return Result.getFail().setData(data);
        }
//        System.out.println(fileName);
        String suffixName = Objects.requireNonNull(fileName).substring(index, fileName.length());
        if (
                !".jpg".equalsIgnoreCase(suffixName) &&
                        !".jpeg".equalsIgnoreCase(suffixName) &&
                        !".png".equalsIgnoreCase(suffixName)
        ) {//不为图片类型则返回
            data.put("error", "文件格式错误");
            return Result.getFail().setData(data);
        }
//        suffixName = ".jpg";

        String imgName = user.getId().toUpperCase() + suffixName;
        String imgDir = "/www/wwwroot/resources.Jian-Internet.com/JMask/images/avatars/";
        /*
        File directory = new File("");
        String imgDir = directory.getAbsolutePath() + "/AppData/";
        */
        String saveAvatarPath = imgDir + imgName;
        String avatarURL = "https://resources.jian-internet.com:50000/JMask/images/avatars/" + imgName;
        File targetFile = new File(imgDir);
        if (!targetFile.exists()) {
            if (!targetFile.mkdirs()) {
                data.put("error", "存储错误");
                return Result.getFail().setData(data);
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(saveAvatarPath);
            out.write(file.getBytes());
            if (userMapper.uploadAvatar(user.getId(), avatarURL) && userMapper.updateHasAvatar(user.getId(), true)){
                return Result.getSuccess();
            }else {
                data.put("error", "数据库错误");
                return Result.getFail().setData(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            data.put("error", "写入错误");
            return Result.getFail().setData(data);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

