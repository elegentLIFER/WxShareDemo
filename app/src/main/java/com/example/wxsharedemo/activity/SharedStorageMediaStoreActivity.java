package com.example.wxsharedemo.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wxsharedemo.R;
import com.example.wxsharedemo.Utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharedStorageMediaStoreActivity extends AppCompatActivity {
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;

    private static final String TAG = "ScopedStorageActivity";

    private Uri queryUri;
    private File newFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_storage_ms);
        ButterKnife.bind(this);

        setTitle("共享目录");
        tvTip.setText("共享目录操作,自己创建的文件无需权限，别人创建的文件需要权限。使用[MediaStore API]操作。" +
                "\n 使用MediaStore API只能操作媒体文件，如果需要操作非媒体文件例如PDF文件，要使用SAF");

        String dcimPath = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_DCIM;
        tvContent.setText("共享目录(以DCIM为例)：" + dcimPath);

    }

    @OnClick({R.id.bt_create, R.id.bt_edit, R.id.bt_read, R.id.bt_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create:
                createImage(view);
                break;
            case R.id.bt_edit:
                break;
            case R.id.bt_read:
                readFile();
                break;
            case R.id.bt_delete:
                deleteFile();
                break;
        }
    }

    public void createImage(View view) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DESCRIPTION, "描述");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.IS_PENDING, 0);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "test.png");
        values.put(MediaStore.Images.Media.TITLE, "Image.png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/wt");

        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri insertUri = getContentResolver().insert(external, values);
        OutputStream os = null;
        try {
            if (insertUri != null) {
                os = getContentResolver().openOutputStream(insertUri);
            }
            if (os != null) {
                Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                int color = Color.RED;
                canvas.drawColor(color);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);

                if (insertUri != null) {
                    values.clear();
                    getContentResolver().update(insertUri, values, null, null);
                }
            }
            tvContent.setText("创建成功");
        } catch (IOException e) {
            tvContent.setText("创建失败" + e.getMessage());
        } finally {
            Utils.closeIO(os);
        }
    }

    public void readFile() {
        queryUri = queryUri("test.png");
        if (queryUri == null) {
            return;
        }
        tvContent.setText(queryUri.toString());

        ParcelFileDescriptor pfd = null;
        try {
            pfd = getContentResolver().openFileDescriptor(queryUri, "r");
            if (pfd != null) {
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                ivImage.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeIO(pfd);
        }
    }

    private Uri queryUri(String displayName) {
        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DISPLAY_NAME + "=?";
        String[] args = new String[]{displayName};

        Cursor cursor = getContentResolver().query(external, projection, selection, args, null);
        if (cursor != null && cursor.moveToNext()) {
            long id = cursor.getLong(0);
            Uri queryUri = ContentUris.withAppendedId(external, id);
            Log.e(TAG, "查询成功，Uri路径" + queryUri + "  id:" + id);
            cursor.close();
            return queryUri;
        }
        return null;
    }

    public void deleteFile() {
        if (queryUri == null) return;
        getContentResolver().delete(queryUri, null, null);
        queryUri = null;
        ivImage.setImageBitmap(null);
        tvContent.setText("delete success");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteFile();
    }
}
