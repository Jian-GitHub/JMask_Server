package com.jian.untils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * @author Jian Qi
 * @Date 2021/10/5 8:04 下午
 * @Description 图片处理
 * @Version 1
 */
public class ImgUtil {
    // 对字节数组字符串进行Base64解码并生成图片

    /**
     * 传入base64编码后的图片字符串
     * @param imgData 图片字符串
     * @param savePath 图片保存路径
     * @return
     */
    public static boolean GenerateImage(String imgData, String savePath) {
        if (imgData == null || imgData.equals("")) {
            // 图像数据为空
            return false;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            // Base64解码
            byte[] b = decoder.decode(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
//            String imgFilePath = "/Users/qi/Downloads/images-javafx.png";// 新生成的图片

            OutputStream out = new FileOutputStream(savePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
