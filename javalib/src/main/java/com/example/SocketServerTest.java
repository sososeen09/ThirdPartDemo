package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yunlong.su on 2017/2/16.
 */

public class SocketServerTest {
    public static void main(String[] args) {
        try {
            final ServerSocket serverSocket = new ServerSocket(9999);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            loo(serverSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loo(ServerSocket serverSocket) throws IOException {
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        System.out.println("from client: " + String.valueOf(bytes));

        inputStream.close();
        OutputStream outputStream = accept.getOutputStream();

        outputStream.write("receive client message".getBytes());

        outputStream.close();
    }
}
