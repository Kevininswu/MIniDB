package com.kevin.minidb.transport;

/**
 * @author Kevin Liu
 * @description 服务段和客户段的通信
 *
 * @create 2024/8/17 11:15
 */
public class Package {
    byte[] data;
    Exception err;

    public Package(byte[] data, Exception err) {
        this.data = data;
        this.err = err;
    }

    public byte[] getData() {
        return data;
    }

    public Exception getErr() {
        return err;
    }
}
