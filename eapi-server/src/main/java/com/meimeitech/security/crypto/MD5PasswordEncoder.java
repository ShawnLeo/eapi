package com.meimeitech.security.crypto;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Shawn on 2017/8/15.
 */
public class MD5PasswordEncoder implements PasswordEncoder {

    public static void main(String[] args) {
        String password = "111111";
        System.out.println(MD5.encodeByMd5AndSalt(password));
    }

    @Override
    public String encode(CharSequence charSequence) {
        return MD5.encodeByMd5AndSalt(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}
