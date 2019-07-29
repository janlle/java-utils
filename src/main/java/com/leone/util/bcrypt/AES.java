package com.leone.util.bcrypt;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Scanner;

/**
 * 对称加密算法--AES
 *
 * @author Leone
 * @since 2018-05-01
 **/
@Slf4j
public class AES {

    private static KeyGenerator keyGenerator;

    static {
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String rule = "123";
        String content = "hello";

        String result = encode(content, rule);
        System.out.println("加密后的密文是: " + result);
        result = decode(result, rule);
        System.out.println("解密后的明文是: " + result);
    }

    /**
     * 加密
     *
     * @param content
     * @param rule
     * @return
     */
    public static String encode(String content, String rule) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes(StandardCharsets.UTF_8));
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            Key key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult = cipher.doFinal(content.getBytes());
            return new HexBinaryAdapter().marshal(encodeResult);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content
     * @param rule
     * @return
     */
    public static String decode(String content, String rule) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes(StandardCharsets.UTF_8));
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            Key key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult = cipher.doFinal(DatatypeConverter.parseHexBinary(content));
            return new String(decodeResult);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * @param content
     */
    public static void bcAES(String content) {
        try {
            // 使用BouncyCastle 的DES加密
            Security.addProvider(new BouncyCastleProvider());
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BC");
            keyGenerator.getProvider();
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            //Key转换
            Key key = new SecretKeySpec(keyBytes, "AES");
            //加密
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult = cipher.doFinal(content.getBytes());

            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult = cipher.doFinal(encodeResult);
            System.out.println("AESDecode : " + new String(decodeResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}