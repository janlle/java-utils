package com.leone.util.bcrypt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

/**
 * 非对称加密算法--RSA
 *
 * @author Leone
 * @since 2018-05-01
 **/
@Slf4j
public abstract class RSA {

    private static String src = "{\"appId\":\"109098\",\"nonceStr\":\"nonceStr\",\"timestamp\":1558703431,\"data\":{\"userId\":1001,\"account\":\"1009\",\"password\":\"1209098\",\"description\":\"hello world\",\"age\":18,\"deleted\":false}}";

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4/F9yRVp1xUlb2Os23lkkFjfda9nb0xxUjHlVM5n/N7kug1Mqt0W6+T3S6276sPgXFJSoTYIHLdZ1v5tizMEWX7akutEH/+otopcl7j2+Z/OKT4QR+JAYgQN2Pq3g33azraqMFm8k+m7D9lB0tTnm53TInORIPjHX/qj4PJ4HpQIDAQAB";

    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALj8X3JFWnXFSVvY6zbeWSQWN91r2dvTHFSMeVUzmf83uS6DUyq3Rbr5PdLrbvqw+BcUlKhNggct1nW/m2LMwRZftqS60Qf/6i2ilyXuPb5n84pPhBH4kBiBA3Y+reDfdrOtqowWbyT6bsP2UHS1OebndMic5Eg+Mdf+qPg8ngelAgMBAAECgYEAoLvhb1gltulayazdDIr856dKmWGqJiD0n96DWu4AZEuV432GmTowI7uH5apefwOgPeXLGcZnMje4/g0kRh/lsT/A+l3XrLYosaOifaBBohgcNyysp8d39RtkmasWz3sn2y3ctLSqlt2uPPDUFBgh9v1p2Y3CZYwBOKpJnxfd8GECQQDytbGMXimuPVS+oUQR5J7i+XDccwI/eo5qtWSaNqu7NA9FXqI5VZDlVshfslMIAtLhIEEzWBH4SNbcnxParOCpAkEAwx2BVidCb6CX3XkI5HKu7wMxUkkmirtGXB9WVOMyj7PsqnjG128mLr2ME1aNsfQFlVEfGC2dbAroL03Ds75AnQJAKTnrmqgz9EC+sFK8OT3YLz2nigqPCzFKF54QJJG8weOp5GKas5pxLkN7baXgrK+uGkdcS9hd1QqqVHdA7BgJYQJAHw+WZGGxmNWm93HqMXv0T0Zh1qiggxtXExlGBBN7HBdXiLfbZ7ZhDLXOE9IGkpq3PNMCMTYpxmZiGg22JuoVnQJAbyszLjSEXY8ko/VjsxiYmXWbIrJJ1U/l2mPisLF0RzXAIGGnm8zjibtRtmIGQhUG4Ln+fy7cyZOznc0Cn1jLnA==";

    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    private static Cipher cipher;

    private static KeyFactory keyFactory;

    private static KeyPairGenerator keyPairGenerator;

    static {
        try {
            cipher = Cipher.getInstance("RSA");
            keyFactory = KeyFactory.getInstance("RSA");
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public static void main(String[] args) throws Exception {
        RSAPublicKey rsaPublicKey = loadPublicKeyByStr(PUBLIC_KEY);
        RSAPrivateKey rsaPrivateKey = loadPrivateKeyByStr(PRIVATE_KEY);

        // RSAPublicKey rsaPublicKey = loadPublicKeyByFile("D:\\rsa_public_key.pem");
        // RSAPrivateKey rsaPrivateKey = loadPrivateKeyByFile("D:\\rsa_private_key.pem");

        String privateDate = pubKeyEncode(src, rsaPublicKey);
        System.out.println(privateDate);

        String result = priKeyDecode(privateDate, rsaPrivateKey);
        System.out.println(result);

        // HashMap<String, Object> keys = getKeys(1024);
        // System.out.println("public_key:" + keys.get("public"));
        // System.out.println("private_key:" + keys.get("private"));
        // generatorKeys("D:\\");

    }

    /**
     * 从文件中加载公钥对象
     */
    public static RSAPublicKey loadPublicKeyFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String key = sb.toString();
            // key = key.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
            // key = key.replace("\n-----END RSA PRIVATE KEY-----", "");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoder.decode(key));
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 从文件中加载私钥对象
     *
     * @param filePath
     * @return
     */
    public static RSAPrivateKey loadPrivateKeyFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String key = sb.toString();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoder.decode(key));
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 从字符串中加载公钥对象
     *
     * @param publicKey
     * @return
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoder.decode(publicKey));
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 从字符串中加载私钥对象
     *
     * @param privateKey
     * @return
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoder.decode(privateKey));
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 私钥加密
     *
     * @param content
     * @param pri_key
     * @return
     */
    public static String priKeyEncode(String content, RSAPrivateKey pri_key) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pri_key);
            byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return encoder.encodeToString(bytes);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param content
     * @param pub_key
     * @return
     */
    public static String pubKeyEncode(String content, RSAPublicKey pub_key) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pub_key);
            byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return encoder.encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 私钥解密
     *
     * @param content
     * @param pri_key
     * @return
     */
    public static String priKeyDecode(String content, RSAPrivateKey pri_key) {
        try {
            byte[] bytes = decoder.decode(content);
            cipher.init(Cipher.DECRYPT_MODE, pri_key);
            return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 公钥解密
     *
     * @param content
     * @param pub_key
     * @return
     */
    public static String pubKeyDecode(String content, RSAPublicKey pub_key) {
        try {
            byte[] bytes = decoder.decode(content);
            cipher.init(Cipher.DECRYPT_MODE, pub_key);
            return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 获取公钥私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static HashMap<String, Object> getKeys(Integer length) throws NoSuchAlgorithmException {
        if (length % 64 != 0) {
            throw new RuntimeException("模值长度必须是64的倍数");
        }
        HashMap<String, Object> map = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        map.put("public", encoder.encodeToString(publicKey.getEncoded()));
        map.put("private", encoder.encodeToString(privateKey.getEncoded()));
        return map;
    }

    /**
     * 生成公钥私钥
     *
     * @param path
     */
    public static void generatorKeys(String path) {
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        try {
            String pubKey = encoder.encodeToString(publicKey.getEncoded());
            String priKey = encoder.encodeToString(privateKey.getEncoded());
            BufferedWriter pubFileWriter = new BufferedWriter(new FileWriter(path + "/pub_key.pen"));
            BufferedWriter priFileWriter = new BufferedWriter(new FileWriter(path + "/pri_key.pen"));
            pubFileWriter.write(pubKey);
            priFileWriter.write(priKey);
            pubFileWriter.flush();
            priFileWriter.flush();
            pubFileWriter.close();
            priFileWriter.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}