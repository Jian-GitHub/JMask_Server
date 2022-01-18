package com.jian.controller;

import com.jian.untils.Base64Util;
import com.jian.untils.HttpClientUtil;
import com.jian.untils.ImgUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Jian Qi
 * @Date 2021/10/5 3:07 下午
 * @Description 识别图片
 * @Version 1.0
 */

@RestController
@RequestMapping("JMask/DealData")
public class Img_Controller {

    /**
     * @param userName 用户名
     * @param imgData  图片数据
     * @return 返回处理后的图片Base64编码数据
     * @throws UnsupportedEncodingException 抛出处理过程中的异常
     */
    @RequestMapping(
//            method = {RequestMethod.POST},
            value = "/dealRTM"
    )
    public String dealRTMClient(@RequestParam(value = "userName", required = false) String userName, @RequestParam("imgData") String imgData) throws UnsupportedEncodingException {
//      将图片Base64数据传至Python内处理，接收处理后的数据。
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("imgData", imgData);
            //向服务器传送用户名，图片类型，图片数据，接收处理后的图片数据Base64编码
            String result_imgData = HttpClientUtil.doPost("http://127.0.0.1:5000/Mask", hashMap);
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
     * @param userName 用户名
     * @param imgData  图片数据
     * @param imgType  图片类型(文件后缀)
     * @return 返回处理后的图片Base64编码数据
     * @throws UnsupportedEncodingException 抛出处理过程中的异常
     */
    @RequestMapping(
//            method = {RequestMethod.POST},
            value = "/dealImg"
    )
    public String dealImgClient(@RequestParam("userName") String userName, @RequestParam("imgData") String imgData, @RequestParam("imgType") String imgType) throws UnsupportedEncodingException {
        File directory = new File("");
        String imgDir = directory.getAbsolutePath() + "/AppData/" + userName;
//        imgData = Base64Util.decode(imgData);
        File file = new File(imgDir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }

        String imgName = String.valueOf(System.currentTimeMillis() / 1000);
        imgDir += "/" + imgName + "." + imgType;

        //存储图片
        ImgUtil.GenerateImage(imgData, imgDir);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("imgData", imgData);
        //向服务器传送用户名，图片类型，图片数据，接收处理后的图片数据Base64编码
        String result_imgData = HttpClientUtil.doPost("http://127.0.0.1:5000/Mask", hashMap);
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
     * @return 返回处理后的图片Base64编码数据
     */
    @RequestMapping(
//            method = {RequestMethod.POST},
            value = "/dealImgWeb"
    )
    public String dealImgWeb(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int index = Objects.requireNonNull(file.getOriginalFilename()).indexOf(".");
        String suffixName = Objects.requireNonNull(fileName).substring(index, fileName.length());

        File directory = new File("");
        String imgName = System.currentTimeMillis() / 1000 + Objects.requireNonNull(fileName).substring(0, index);
        String imgDir = directory.getAbsolutePath() + "/AppData/" + "Web/";
        String fileURL;
        File targetFile = new File(imgDir);
        String imageData = null;
        if (!targetFile.exists()) {
            if (!targetFile.mkdirs()) {
                return null;
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imgDir + imgName + suffixName);
            out.write(file.getBytes());
            fileURL = directory.getAbsolutePath() + "/AppData/" + "Web/" + imgName + suffixName;
            String imgData = Base64Util.ImageToBase64String(fileURL);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("imgData", imgData);
            //向服务器传送用户名，图片类型，图片数据，接收处理后的图片数据Base64编码
            imageData = HttpClientUtil.doPost("http://127.0.0.1:5000/Mask", hashMap);
            if ("".equals(imageData) || imageData == null) {
                return "";
            }
        } catch (IOException e) {
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
        return imageData;
    }
}
