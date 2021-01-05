package com.example.wxsharedemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wxsharedemo.R;
import com.example.wxsharedemo.Utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharedStorageSAFActivity extends AppCompatActivity {
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;

    private static final int REQUEST_CODE_FOR_SINGLE_FILE = 1;
    private static final int WRITE_REQUEST_CODE = 2;
    private Uri queryUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_storage_saf);
        ButterKnife.bind(this);

        initTitle();
    }

    private void initTitle() {
        setTitle("共享目录");
        tvTip.setText("使用[SAF API]操作。\n" +
                "SAF是指Storage Access Framework。\n"+"借助 SAF，用户可轻松在其所有首选文档存储提供程序中浏览并打开文档、图像及其他文件。" +
                "用户可通过易用的标准界面，以统一方式在所有应用和提供程序中浏览文件，以及访问最近使用的文件。\n"
                + "注意：SAF方式在android Q上可以操作私有目录，android R上只能操作共享目录");
    }

    @OnClick({R.id.bt_create, R.id.bt_edit, R.id.bt_read, R.id.bt_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create:
                createFile();
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

    public void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TITLE, "test");
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    public void readFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_SINGLE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_SINGLE_FILE && resultCode == RESULT_OK) {
            queryUri = data.getData();
            tvContent.setText("读取成功：" + queryUri.toString());

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
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deleteFile() {
        try {
            boolean deleted = DocumentsContract.deleteDocument(getContentResolver(), queryUri);
            if (deleted) {
                ivImage.setImageBitmap(null);
                tvContent.setText("删除成功");
            } else {
                tvContent.setText("删除失败");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            tvContent.setText("删除失败: " + e.getMessage());
        }
    }

}
