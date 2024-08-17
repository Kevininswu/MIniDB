package com.kevin.minidb.backend.utils;

/**
 * @author Kevin Liu
 * @description 创建UID
 * @create 2024/7/28 15:18
 */
public class Types {
    public static long addressToUid(int pgno, short offset) {
        long u0 = (long)pgno;
        long u1 = (long)offset;
        return u0 << 32 | u1;
    }
}
