package com.example.wxsharedemo.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wxsharedemo.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScopedStorageActivity extends AppCompatActivity {
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private static final String TAG = "ScopedStorageActivity";

    private File newFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoped_storage);
        ButterKnife.bind(this);

        setTitle("私有目录");
        tvTip.setText("私有目录操作,无需权限。使用[File API]操作");

        File externalCacheDir = getExternalCacheDir();
        Log.e(TAG, "getPath: " + externalCacheDir.getPath());
        tvContent.setText("私有目录：" + externalCacheDir.getPath());

        String newFilePath = externalCacheDir.getPath() + File.separator + "text.txt";
        newFile = new File(newFilePath);
    }

    @OnClick({R.id.bt_create, R.id.bt_edit, R.id.bt_read, R.id.bt_delete, R.id.bt_access_other_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create:
                createFile(newFile);
                break;
            case R.id.bt_edit:
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newFile));
                    bufferedWriter.write("hello world");
                    bufferedWriter.close();
                    tvContent.setText("写入成功");
                } catch (IOException e) {
                    e.printStackTrace();
                    tvContent.setText("写入失败: " + e.getMessage());
                }
                break;
            case R.id.bt_read:
                readFile(newFile);
                break;
            case R.id.bt_delete:
                if (newFile.exists()) {
                    boolean delete = newFile.delete();
                    tvContent.setText("删除成功？: " + delete);
                } else {
                    tvContent.setText("已经删除");
                }
                break;
            case R.id.bt_access_other_app:
                String newFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Android/data/com.example.filereceiver/" + "text.txt";
                createFile(new File(newFilePath));
//                readFile(new File(newFilePath));
                break;
        }
    }

    private void createFile(File file) {
        boolean exists = file.exists();
        if (!exists) {
            try {
                boolean createSuccess = file.createNewFile();
                tvContent.setText("创建成功?: " + createSuccess + "\n" + file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                tvContent.setText(e.getMessage());
            }
        } else {
            tvContent.setText("已经存在" + "\n" + file.getPath());
        }
    }

    private void readFile(File file) {
        try {
            tvContent.setText("");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String readLine = bufferedReader.readLine();
            tvContent.setText(readLine);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            tvContent.setText("读取失败：" + e.getMessage());
        }
    }
}
