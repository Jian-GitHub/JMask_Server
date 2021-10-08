package com.jian.controller;

import com.jian.untils.Base64Util;
import com.jian.untils.HttpClientUtil;
import com.jian.untils.ImgUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.UnsupportedEncodingException;

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
     *
     * @param userName
     * @param imgData
     * @param imgType
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(
//            method = {RequestMethod.POST},
            value = "/dealImg"
    )
    public String dealImg(@RequestParam("userName") String userName, @RequestParam("imgData") String imgData, @RequestParam("imgType") String imgType) throws UnsupportedEncodingException {
        File directory = new File("");
        String imgDir = directory.getAbsolutePath() + "/AppDatas/" + Base64Util.decode(userName);
        imgData = Base64Util.decode(imgData);
        File file = new File(imgDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        String imgName = String.valueOf(System.currentTimeMillis() / 1000);
        imgDir += "/" + imgName + "." + imgType;

        ImgUtil.GenerateImage(imgData, imgDir);
//        System.out.println(imgDir);

        String url = "http://127.0.0.1:9000/Mask?mode=img&imgdir=" + Base64Util.encode(imgDir);
        String imageData = HttpClientUtil.doPost(url);


//        String fname = "/Users/qi/Downloads/images-javafx.jpg";
//        int pos = fname.lastIndexOf('/');
//        if (pos > -1)
//            fname.substring(0, pos);
////        System.out.println(fname.substring(0, pos));
//        System.out.println(fname.substring(pos));
        return imageData;
    }
}
