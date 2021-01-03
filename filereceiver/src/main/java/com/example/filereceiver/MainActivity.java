package com.example.filereceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tvContent;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getExternalCacheDir();

        tvContent = findViewById(R.id.tv_content);
        ivImage = findViewById(R.id.iv_image);

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "DCIM/test.jpg");
        boolean b = file.canRead();
        boolean exists = file.exists();
        Log.e(TAG, "onCreate: " + b + "  " + exists);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Uri uri = (Uri) extras.get(Intent.EXTRA_STREAM);

//            Cursor c = getContentResolver().query(uri, null, null, null, null);
//            if (null != c && c.moveToFirst()) {
//                String path = getRealFilePath(this, uri);
//                File file = new File(path);
//                boolean exists = file.exists();
//                boolean b = file.canRead();
//                Log.e(TAG, "exists: "+ exists);
//                Log.e(TAG, "canRead: "+ b);
////                if (exists) {
//                    tvContent.setText(path);
//
//            String realFilePath = getRealFilePath(this, uri);
//            Log.e(TAG, "onCreate: " + realFilePath);

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ivImage.setImageBitmap(bitmap);
//                }
//            }
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}