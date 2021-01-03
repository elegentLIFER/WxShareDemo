package com.example.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = Uri.parse("content://com.example.contentprovider/user");
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id",3);
        contentValues.put("name", "iverson");

        ContentResolver contentResolver = getContentResolver();
        contentResolver.insert(uri,contentValues);

        Cursor cursor = contentResolver.query(uri, new String[]{"_id", "name"}, null, null, null);
        while (cursor.moveToNext()) {
            // 将表中数据全部输出
            ((TextView) findViewById(R.id.tv_content)).setText("query book:" + cursor.getInt(0) +" "+ cursor.getString(1));
        }
        cursor.close();


    }
}