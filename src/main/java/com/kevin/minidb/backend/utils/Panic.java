package com.kevin.minidb.backend.utils;

/**
 * @author Kevin Liu
 * @description
 * @create 2024/7/27 9:34
 */
public class Panic {
    public static void panic(Exception exception){
        exception.printStackTrace();
        System.exit(1);

    }
}
