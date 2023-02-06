package com.jian.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jian.entity.Result;
import com.jian.entity.User;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.HttpClientUtil;
import com.jian.untils.ImgUtil;
import com.jian.untils.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.coobird.thumbnailator.Thumbnails;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jian Qi
 * @Date 2021/10/5 3:07 下午
 * @Description 识别图片
 * @Version 1.0
 */

@RestController
@RequestMapping("JMask/DealData")
@Tag(name = "图片相关")
public class Img_Controller {

    private static String pythonServerURL = "http://127.0.0.1:5000/Mask";
    
    final
    User_Mapper user_mapper;

    public Img_Controller(User_Mapper user_mapper) {
        this.user_mapper = user_mapper;
    }

    /**
     * @param userName 用户名
     * @param imgData  图片数据
     * @return (客户端实时)返回处理后的图片Base64编码数据
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/dealRTM"
    )
    @Operation(summary = "实时处理图片 - 客户端")
    @Parameters({
            @Parameter(name = "userName", description = "用户名", required = false),
            @Parameter(name = "imgData", description = "图片数据的Base64编码", required = true)
    })
    public String dealRTMClient(@RequestParam(value = "userName", required = false) String userName, @RequestParam("imgData") String imgData) {
//      将图片Base64数据传至Python内处理，接收处理后的数据。
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("imgData", imgData);
            //向服务器传送用户名，图片类型，图片数据，接收处理后的图片数据Base64编码
            String result_imgData = HttpClientUtil.doPost(pythonServerURL, hashMap);
            if ("".equals(result_imgData) || result_imgData == null) {
                return "";
            }
//            System.out.println(result_imgData);
            return result_imgData;
        } catch (Exception e) {
            e.printStackTrace();
        }


//        String url = "http://127.0.0.1:5000/Mask?imgData=" + encoder.encodeToString(data);
////        String url = "http://127.0.0.1:5000/Mask?imgData=" + "YWFhYWE=";
//        String result = HttpClientUtil.doGet(url);
//        System.out.println(result);
        return "result";
    }

    /**
     * @param token   用户token
     * @param imgData 图片数据
     * @param imgType 图片类型(文件后缀)
     * @return (客户端)返回处理后的图片Base64编码数据
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/dealImg"
    )
    @Operation(summary = "处理图片 - 客户端")
    @Parameters({
            @Parameter(name = "token", description = "用户token信息", required = true),
            @Parameter(name = "imgData", description = "图片数据的Base64编码", required = true),
            @Parameter(name = "imgType", description = "图片格式", required = true, example = "jpg")
    })
    public String dealImgClient(
//            @RequestParam("userName") String userName,
            @RequestParam("token") String token,
            @RequestParam("imgData") String imgData,
            @RequestParam("imgType") String imgType
    ) {
        DecodedJWT verify;
        Map<String, String> data = new HashMap<>();
        if (token == null) {
            data.put("error", "Token为空");
            return "";
        }
        try {
            verify = JWTUtils.verify(token);
        } catch (Exception e) {
            data.put("error", "Token无效");
            return "";
        }
        User user = user_mapper.selectUserByID(verify.getClaim("id").asString());
        if (user == null) {
            data.put("error", "用户无效");
            return "";
        }

        File directory = new File("");
        String imgDir = directory.getAbsolutePath() + "/AppData/" + user.getId();
//        imgData = Base64Util.decode(imgData);
        File file = new File(imgDir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }

        //开始处理

        String imgName = System.currentTimeMillis() / 1000 + "." + imgType;
        imgDir += File.separator + imgName;

        //存储图片
        ImgUtil.GenerateImage(imgData, imgDir);
        //写入记录
        user_mapper.addUserLog(user.getId(), imgName);
        //识别处理
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("imgData", imgData);
        //向服务器传送用户名，图片类型，图片数据，接收处理后的图片数据Base64编码
        String result_imgData = HttpClientUtil.doPost(pythonServerURL, hashMap);
        if ("".equals(result_imgData) || result_imgData == null) {
            return "";
        }
//            System.out.println(result_imgData);
        return result_imgData;


////      将图片路径传至Python内处理，接收处理后的数据。
//        String url = "http://127.0.0.1:5000/Mask?imgData=" + imgData;
//        return HttpClientUtil.doPost(url);
    }

    /**
     * @param file 网页传入的文件(图片)
     * @return (网页端)返回处理后的图片Base64编码数据
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/dealImgWeb"
    )
    @Operation(summary = "处理图片 - Web端")
    @Parameters({
            @Parameter(name = "file", description = "上传的图片文件", required = true),
            @Parameter(name = "token", description = "图片数据的Base64编码", required = false)
    })
    public Result<Map<String, String>> dealImgWeb(@RequestParam("file") MultipartFile file, @RequestParam(value = "token", required = false) String token) {
        boolean isLoginUser = false;
        User user = null;
        String imageData;
        Map<String, String> resultData = new HashMap<>(16);

        String fileName = file.getOriginalFilename();
        int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
        if ("".equals(fileName) || fileName == null || index <= 0) {
            resultData.put("error", "文件为空");
            System.gc();
            return Result.getFail().setData(resultData);
        }
        String suffixName = Objects.requireNonNull(fileName).substring(index, fileName.length());
        if (
                !".jpg".equalsIgnoreCase(suffixName) &&
                        !".jpeg".equalsIgnoreCase(suffixName) &&
                        !".png".equalsIgnoreCase(suffixName) &&
                        !".heic".equalsIgnoreCase(suffixName)
        ) {//不为图片类型则返回空字符串
            resultData.put("error", "图片类型有误");
            System.gc();
            return Result.getFail().setData(resultData);
        }
//        suffixName = ".jpg";

        File directory = new File("");
        String imgName = System.currentTimeMillis() / 1000 + "_" + Objects.requireNonNull(fileName).substring(0, index);
        fileName = null;
        String imgDir = directory.getAbsolutePath() + "/AppData/" + "Web/";

        //验证token
        if (!"".equals(token)) {
            try {
                DecodedJWT verify = JWTUtils.verify(token);
                user = user_mapper.selectUserByID(verify.getClaim("id").asString());
                verify = null;
                if (user != null) {
                    imgDir = directory.getAbsolutePath() + "/AppData/" + user.getId();
                    directory = null;
                    imgName = String.valueOf(System.currentTimeMillis() / 1000);
                    isLoginUser = true;
                }
            } catch (Exception e) {
                resultData.put("error", "登录信息失效，请重新登录\n(本次识别不计入记录)");
            }
        }

        String filePath;
        File targetFile = new File(imgDir);
        if (!targetFile.exists()) {
            if (!targetFile.mkdirs()) {
                resultData.put("error", "文件处理失败");
                System.gc();
                return Result.getFail().setData(resultData);
            }
        }

        FileOutputStream out = null;
        filePath = imgDir + File.separator + imgName + suffixName;

        try {
            // 待处理的图片数据
            String imgData = null;
            // 存储图片
            out = new FileOutputStream(filePath);
            out.write(file.getBytes());

            // 如果是heic文件，则进行转换
            if (".heic".equalsIgnoreCase(suffixName)) {
                suffixName = ".jpg";
                String newImgFilePath = imgDir + File.separator + imgName + suffixName;
                dealHeic(filePath, newImgFilePath);
                filePath = newImgFilePath;
                imgData = Base64Util.ImageToBase64String(filePath);
                resultData.put("originImgData", imgData);
            } else {
                imgData = Base64Util.ImageToBase64String(filePath);
            }
            filePath = null;
            imgDir = null;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("imgData", imgData);
            imgData = null;
            //向服务器传送用户名，图片类型，图片数据，接收处理后的图片数据Base64编码
            imageData = HttpClientUtil.doPost(pythonServerURL, hashMap);
            //无法正确处理则返回空字符串
            if (imageData == null) {
                resultData.put("error", "识别失败");
                System.gc();
                return Result.getFail().setData(resultData);
            } else {
                resultData.put("imageData", imageData);
                imageData = null;
                //是登录用户则写入记录
                if (isLoginUser && user != null) {
                    user_mapper.addUserLog(user.getId(), imgName + suffixName);
                    imgName = null;
                    suffixName = null;
                    user = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        System.gc();
        return Result.getSuccess().setData(resultData);
    }

    /**
     *
     * @param heicPath heic图片文件路径
     * @param newImgFilePath 目标图片文件路径
     * @throws Exception
     */
    @Operation(summary = "处理heic图片 - 转为指定目标图片文件")
    @Parameters({
            @Parameter(name = "heicPath", description = "heic图片文件路径", required = true),
            @Parameter(name = "newImgFilePath", description = "目标图片文件路径", required = true)
    })
    private void dealHeic(@RequestParam("heicPath") String heicPath, @RequestParam("newImgFilePath") String newImgFilePath) throws Exception {
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();
//        cmd.setSearchPath("/Users/jian/code/7.1.0-49_1");
        op.addImage(heicPath);
        op.addImage(newImgFilePath);
        cmd.run(op);
        File oldImgFile = new File(heicPath); //删除heic图片文件
        if (oldImgFile.exists()) {
            oldImgFile.delete();
        }
    }
}
