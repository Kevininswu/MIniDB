package com.kevin.minidb.client;

import java.util.Scanner;

/**
 * @author Kevin Liu
 * @description
 * @create 2024/8/17 13:49
 */
public class Shell {
    private Client client;

    public Shell(Client client) {
        this.client = client;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        try {
            while(true) {
                System.out.print(":> ");
                String statStr = sc.nextLine();
                if("exit".equals(statStr) || "quit".equals(statStr)) {
                    break;
                }
                try {
                    byte[] res = client.execute(statStr.getBytes());
                    System.out.println(new String(res));
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        } finally {
            sc.close();
            client.close();
        }
    }
}