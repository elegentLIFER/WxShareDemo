package com.example.wxsharedemo;

import java.io.Closeable;
import java.io.IOException;

public class Utils {
    public static void closeIO(Closeable io) {
        try {
            io.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
