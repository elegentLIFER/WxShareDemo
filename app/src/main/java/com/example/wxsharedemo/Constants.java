package com.example.wxsharedemo;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Constants {
    public static String getDataPath(Context context) {
        File dir = context.getExternalCacheDir();
        return dir.getAbsolutePath();
    }

    public static String getOutterPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "A";
    }
}
