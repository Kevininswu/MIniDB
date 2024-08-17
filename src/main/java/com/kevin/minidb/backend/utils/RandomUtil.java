package com.kevin.minidb.backend.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Kevin Liu
 * @description 生成length长度的随机数组
 * @create 2024/7/27 15:12
 */
public class RandomUtil {
    public static byte[] randomBytes(int length) {
        Random r = new SecureRandom();
        byte[] buf = new byte[length];
        r.nextBytes(buf);
        return buf;
    }
}
