package com.leone.util.bcrypt;

import java.util.Base64;

/**
 * @author Leone
 * @since 2018-05-10
 **/
public abstract class Base_64 {

    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    public static void main(String[] args) {
        String content = "hello world";

        String encode = encode(content);
        System.out.println(encode);

        String decode = decode(encode);
        System.out.println(decode);
    }


    /**
     * JDK8 base64 编码
     *
     * @param content
     */
    public static String encode(String content) {
        try {
            return new String(encoder.encode(content.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JDK8 base64 解码
     *
     * @param content
     */
    public static String decode(String content) {
        try {
            return new String(decoder.decode(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}