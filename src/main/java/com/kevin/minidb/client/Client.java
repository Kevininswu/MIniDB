package com.kevin.minidb.client;

import com.kevin.minidb.transport.Packager;
import com.kevin.minidb.transport.Package;
/**
 * @author Kevin Liu
 * @description client的执行
 * @create 2024/8/17 13:46
 */
public class Client {
    private RoundTripper rt;

    public Client(Packager packager) {
        this.rt = new RoundTripper(packager);
    }

    public byte[] execute(byte[] stat) throws Exception {
        Package pkg = new Package(stat, null);
        Package resPkg = rt.roundTrip(pkg);
        if(resPkg.getErr() != null) {
            throw resPkg.getErr();
        }
        return resPkg.getData();
    }

    public void close() {
        try {
            rt.close();
        } catch (Exception e) {
        }
    }

}
