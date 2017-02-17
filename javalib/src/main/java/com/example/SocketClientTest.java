package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by yunlong.su on 2017/2/16.
 */

public class SocketClientTest {
    public static void main(String[] args) {
        final Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("127.0.0.1", 9999));

        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    InputStream inputStream = null;
                    try {
                        inputStream = socket.getInputStream();

                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write("Hello World".getBytes());

                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
}
