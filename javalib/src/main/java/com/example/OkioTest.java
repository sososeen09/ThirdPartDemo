package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Created by yunlong.su on 2017/2/15.
 */

public class OkioTest {
    public static void main(String[] args) {
        Source source;
        BufferedSource bufferedSource = null;
        File file = new File("javalib/hello.txt");
        String s = null;
        try {
            source = Okio.source(file);
            bufferedSource = Okio.buffer(source);
            s = bufferedSource.readUtf8();
            System.out.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Sink sink;
        BufferedSink bufferedSink = null;
        File file1 = new File("javalib/copy.txt");
        try {
            bufferedSink = Okio.buffer(Okio.sink(file1));
            bufferedSink.writeUtf8(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedSink.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
