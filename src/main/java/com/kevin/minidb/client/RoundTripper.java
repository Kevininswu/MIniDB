package com.kevin.minidb.client;

import com.kevin.minidb.transport.Packager;
import com.kevin.minidb.transport.Package;

/**
 * @author Kevin Liu
 * @description 发送消息并接受响应
 * @create 2024/8/17 13:44
 */
public class RoundTripper {
    private Packager packager;

    public RoundTripper(Packager packager) {
        this.packager = packager;
    }

    public Package roundTrip(Package pkg) throws Exception {
        packager.send(pkg);
        return packager.receive();
    }

    public void close() throws Exception {
        packager.close();
    }
}