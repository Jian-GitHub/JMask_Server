package com.jian.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jian.entity.Log;
import com.jian.entity.Result;
import com.jian.entity.User;
import com.jian.mapper.Avatar_Mapper;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.HmacSHA512_Util;
import com.jian.untils.JWTUtils;
import com.jian.untils.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.scheduling.annotation.Async;
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
@Tag(name = "用户相关")
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

    @RequestMapping(method = RequestMethod.POST, value = "select")
    @Operation(summary = "获取全部用户")
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
    @Operation(summary = "通过Token获得用户密码")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
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
    @Operation(summary = "更新用户名")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true),
            @Parameter(name = "newUserName", description = "新用户名", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result updateUserNameByToken(@RequestParam(value = "token") String token, @RequestParam(value = "newUserName") String newUserName) {
        DecodedJWT verify = checkToken(token);
        Map<String, String> data = new HashMap<>();
        if (verify == null) {
            data.put("error", "Token无效");
            return Result.getFail().setData(data);
        }
        boolean result = userMapper.updateUserName(verify.getClaim("id").asString(), newUserName);
        if (result) {
            return Result.getSuccess();
        } else {
            return Result.getFail();
        }
    }


    /**
     * 通过ID查询用户
     * @param ID 用户ID
     * @return 用户类JSON Base64编码 字符串
     */
    @RequestMapping(method = RequestMethod.POST, value = "selectByUserID")
    @Operation(summary = "通过ID查询用户")
    @Parameters({
            @Parameter(name = "ID", description = "用户ID", required = true)
    })
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

    /**
     * 通过用户名查询用户
     * @param name 用户名
     * @return 用户类
     */
    @RequestMapping(method = RequestMethod.POST, value = "selectByUserName")
    @Operation(summary = "通过用户名查询用户")
    @Parameters({
            @Parameter(name = "name", description = "用户名", required = true)
    })
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
    @Operation(summary = "更新用户密码")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true),
            @Parameter(name = "newPassWord", description = "加密并Base64编码的新密码", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result updateUserPassWord(@RequestParam(value = "token") String token, @RequestParam(value = "newPassWord") String newPassWord) {
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
        boolean result = userMapper.updatePassWord(user.getId(), newPassWord);
        if (result) {
            return Result.getSuccess();
        } else {
            return Result.getFail();
        }
    }


    /**
     * 通过Token获取用户信息 - id userName avatarURL
     *
     * @param token
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "getUserInfoByToken")
    @Operation(summary = "通过token获取用户信息 - id userName avatarURL")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
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

    /**
     * 通过用户名获取用户信息 - id userName avatarURL
     *
     * @param userName
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "getUserAvatarByUserName")
    @Operation(summary = "通过用户名获取用户信息 - id userName avatarURL")
    @Parameters({
            @Parameter(name = "userName", description = "用户名的Base64编码", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result getUserInfoByUserName(@RequestParam(value = "userName") String userName) {
        Map<String, String> data = new HashMap<>();
//        System.out.println(Base64Util.decode(userName));
        User user = userMapper.selectUserByName(Base64Util.decode(userName));
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

    /**
     * 通过Token获取用户
     *
     * @param token
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "getUserIdByToken")
    @Operation(summary = "通过token获取用户")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
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

    /**
     * 注销用户
     *
     * @param token 用户token
     * @param userName 用户名
     * @param passWord 用户密码
     * @param encodePassWord 加密后的密码
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "removeAccount")
    @Operation(summary = "注销账户")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true),
            @Parameter(name = "userName", description = "用户名", required = true),
            @Parameter(name = "passWord", description = "用户密码", required = true),
            @Parameter(name = "encodePassWord", description = "加密后的密码", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result removeAccount(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "passWord") String passWord,
            @RequestParam(value = "encodePassWord") String encodePassWord
    ) {
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
        if (user.isHasAvatar()) {
            String avatarURL = avatarMapper.selectAvatarByUserID(user.getId());
            String[] avtarURLArray = avatarURL.split("/");
            String avatarPath = "/www/wwwroot/resources.Jian-Internet.com/JMask/images/avatars/" + avtarURLArray[avtarURLArray.length - 1];
//            System.out.println(avatarPath);
            File file = new File(avatarPath);
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        boolean result = userMapper.removeAccount(user.getId());
        if (result) {
            return Result.getSuccess();
        }
        return Result.getFail();
    }

    /**
     * @param file  用户上传的头像(图片)文件
     * @param token 用户token
     * @return
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/uploadAvatar"
    )
    @Operation(summary = "更新用户头像")
    @Parameters({
            @Parameter(name = "file", description = "图片文件", required = true),
            @Parameter(name = "token", description = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
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
            if (userMapper.uploadAvatar(user.getId(), avatarURL) && userMapper.updateHasAvatar(user.getId(), true)) {
                return Result.getSuccess();
            } else {
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


    /**
     * @param avatarBase64  用户上传的头像(图片)Base64编码
     * @param token 用户token
     * @return
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/uploadAvatarBase64"
    )
    @Operation(summary = "获取用户加密后的密码")
    @Parameters({
            @Parameter(name = "avatarBase64", description = "图片Base64编码", required = true),
            @Parameter(name = "token", description = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result uploadAvatar(@RequestParam("avatarBase64") String avatarBase64, @RequestParam("token") String token) {
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
        String saveAvatarURL = "/www/wwwroot/resources.Jian-Internet.com/JMask/images/avatars/" + user.getId() + ".jpg";
        String avatarURL = "https://resources.jian-internet.com:50000/JMask/images/avatars/" + user.getId() + ".jpg";
        if(Base64Util.StringToSaveImage(avatarBase64, saveAvatarURL)){
            if (userMapper.uploadAvatar(user.getId(), avatarURL) && userMapper.updateHasAvatar(user.getId(), true)) {
                return Result.getSuccess();
            }
        }
        return Result.getFail();
    }

    /**
     * 查询用户识别记录的单张图片
     * @param token 用户token
     * @param imgName 查询的图片
     * @return 该图片的Base64编码
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/getLogImgBase64"
    )
    @Operation(summary = "查询用户识别记录的单张图片")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true),
            @Parameter(name = "imgName", description = "图片名", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result getLogImgBase64(@RequestParam("token") String token, @RequestParam("imgName") String imgName){
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
            data.put("error", "用户无效");
            return Result.getFail().setData(data);
        }
        File directory = new File("");
        String imgDir = directory.getAbsolutePath() + "/AppData/" + user.getId() + "/" + imgName;
        File file = new File(imgDir);
        if (!file.exists()) {
            data.put("error", "图片不存在");
            return Result.getFail().setData(data);
        }
        String imgDataBase64 = Base64Util.ImageToBase64String(imgDir);
        data.put("imgDataBase64", imgDataBase64);
        return Result.getSuccess().setData(data);
    }

    /**
     * 通过token获取识别记录
     * @param token 用户token
     * @return
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/getLog"
    )
    @Operation(summary = "获取识别记录")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true),
            @Parameter(name = "startIndex", description = "识别记录开始序号", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result getLog(@RequestParam("token") String token, @RequestParam("startIndex") String startIndex) {
        DecodedJWT verify;
        Map<String, Object> data = new HashMap<>();
        if (token == null) {
            return Result.getFail();
        }
        try {
            verify = JWTUtils.verify(token);
        } catch (Exception e) {
            return Result.getFail().setData("Token无效");
        }
        User user = userMapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            return Result.getFail().setData("用户无效");
        }
        List<Log> logList = userMapper.selectUserLog(user.getId(),Integer.valueOf(startIndex),5);
        if (logList != null && logList.size() > 0) {
            for (Log log : logList) {
                File directory = new File("");
                String imgDir = directory.getAbsolutePath() + "/AppData/" + user.getId() + "/" + log.getImgName();
                File file = new File(imgDir);
                if (!file.exists()) {
                    log.setImgName("");
                    continue;
                }
                String imgData = Base64Util.ImageToBase64String(imgDir);
                log.setImgName(imgData);
            }
        }
        data.put("logList", logList);
        int logNum = userMapper.countUserLog(user.getId());
        data.put("logNum", logNum);
        return Result.getSuccess().setData(data);
    }

    /**
     * 用户退出登录
     * @param token 用户token信息
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/logOut"
    )
    @Operation(summary = "用户退出登录")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true)
    })
    @Async
    public void logOut(@RequestParam("token") String token) {
        if (token == null || "".equals(token)) {
            System.out.println("null");
            return ;
        }
        String userID = JWTUtils.verifyUserToken(token);
        if (userID == null || "".equals(userID)) {
            return ;
        }
        User user = userMapper.selectUserByID(userID);
        if (user == null) {
            return ;
        }
        userMapper.updateUserLoginState(false, userID);
    }
}