package com.jian.untils;
/**
 * @author qi
 * Base64 加密 解密 工具
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
     * @return String base64编码后的图片字符串
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
        }
        // 加密
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}
