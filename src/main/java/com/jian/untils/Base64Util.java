package com.jian.untils;
/**
 * @author qi
 * Base64 加密 解密 工具
 */

import java.io.*;
import java.util.Base64;

public class Base64Util {

    /**
     *
     * @param text 需要加密的文本 String
     * @return 返回加密后的文本 String
     * @throws UnsupportedEncodingException
     */
    public static String encode(String text) throws UnsupportedEncodingException {
        Base64.Decoder decoder = Base64.getDecoder();
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = text.getBytes("UTF-8");
        String encodedText = encoder.encodeToString(textByte);
        return encodedText;
    }

    /**
     *
     * @param text 需要解密的文本 String
     * @return 返回解密后的文本 String
     */
    public static String decode(String text) {
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedText = new String(decoder.decode(text));
        return decodedText;
    }

    /**
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     * @return String base64编码后的图片字符串,错误则返回空字符串""
     */
    public static String ImageToBase64String(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        // 加密
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * 传入base64编码后的图片字符串
     * @param imgData 图片字符串
     * @param savePath 图片保存路径
     * @return
     */
    public static boolean StringToSaveImage(String imgData, String savePath) {
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
