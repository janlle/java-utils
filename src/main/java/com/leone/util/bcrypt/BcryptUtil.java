package com.leone.util.bcrypt;

import org.mindrot.jbcrypt.BCrypt;

/**
 * <p> 用户密码加解密
 *
 * @author leone
 * @since 2019-04-26
 **/
public class BcryptUtil {

    public static String encode(String code) {
        return BCrypt.hashpw(code, BCrypt.gensalt());
    }

    public static void main(String[] args) {
        System.out.println(encode("admin"));
        System.out.println(matching("admin", "$2a$10$ZBRA./kfG14BeHvFKOHDyO1XZqbr.2sO4kOOf/ZLB2tdkigeCNuee"));
    }

    public static boolean matching(String password, String hashed) {
        try {
            return BCrypt.checkpw(password, hashed);
        } catch (Exception e) {
            return false;
        }
    }

}
