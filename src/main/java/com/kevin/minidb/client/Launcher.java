package com.kevin.minidb.client;

import com.kevin.minidb.transport.Encoder;
import com.kevin.minidb.transport.Packager;
import com.kevin.minidb.transport.Transporter;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Kevin Liu
 * @description
 * @create 2024/8/17 13:50
 */
public class Launcher {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        Encoder e = new Encoder();
        Transporter t = new Transporter(socket);
        Packager packager = new Packager(t, e);

        Client client = new Client(packager);
        Shell shell = new Shell(client);
        shell.run();
    }
}
