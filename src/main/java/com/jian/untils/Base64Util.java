package com.jian.untils;
/**
 * @author qi
 * Base64 加密 解密 工具
 */

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
}
