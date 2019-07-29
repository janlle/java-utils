package com.leone.util.bcrypt;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;

/**
 * <p>消息摘要算法--SHA
 *
 * @author Leone
 * @since 2018-07-01
 **/
public class SHA {

    public static void main(String[] args) {
        String content = "hello";
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入要加密的内容:");
//        String input = scanner.nextLine();
        String result = SHA.SHA384(content);
        System.out.println(result);
    }

    /**
     * hash1
     *
     * @param content
     * @return
     */
    public static String SHA1(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            byte[] hashCode = messageDigest.digest(content.getBytes());
            HexBinaryAdapter adapter = new HexBinaryAdapter();
            return adapter.marshal(hashCode).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * hash256
     *
     * @param content
     * @return
     */
    public static String SHA256(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashCode = messageDigest.digest(content.getBytes());
            HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
            return hexBinaryAdapter.marshal(hashCode).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * hash384
     *
     * @param content
     * @return
     */
    public static String SHA384(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
            byte[] hashCode = messageDigest.digest(content.getBytes());
            HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
            return hexBinaryAdapter.marshal(hashCode).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * hash512
     *
     * @param content
     * @return
     */
    public static String SHA512(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hashCode = messageDigest.digest(content.getBytes());
            HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
            return hexBinaryAdapter.marshal(hashCode).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
