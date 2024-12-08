package com.yuanzhi.gateway.utils;


import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Created by zhangding on 2019-05-05.
 */
public class MD5Utils {

    public static String MD5Encode(String origin, Charset charset) {
        return DigestUtils.md5Hex(origin.getBytes(Optional.ofNullable(charset).orElse(StandardCharsets.UTF_8)));
    }

    public static String MD5Encode(String origin) {

        return MD5Encode(origin, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        System.out.println(MD5Encode("155782786402415578278640248831.0bjxws_2019"));
    }
}
