package com.example.wxsharedemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.wxsharedemo.Constants;
import com.example.wxsharedemo.R;
import com.example.wxsharedemo.provider.ZmanFileProvider;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends AppCompatActivity {
    @BindView(R.id.tv_dir)
    TextView tvDir;

    public static final int PERMISSION_REQ_ID = 0x11;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String TAG = "ShareActivity";
    private boolean isData = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        tvDir.setText(Constants.getDataPath(this));

        ((RadioGroup) findViewById(R.id.rg_dir)).setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_data) {
                isData = true;
                ((TextView) findViewById(R.id.tv_dir)).setText(Constants.getDataPath(this));
            } else {
                isData = false;
                ((TextView) findViewById(R.id.tv_dir)).setText(Constants.getOutterPath());
            }
        });
    }

    @OnClick({R.id.bt_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_share:
                if (!isData && !checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE,PERMISSION_REQ_ID)) {
                }else {
                    share();
                }
                break;
        }
    }

    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, getUri());
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "title"));
    }

    private Uri getUri() {
        String imagePath = (isData ? Constants.getDataPath(this) : Constants.getOutterPath()) + File.separator + "test.jpg";
        Uri uri = FileProvider.getUriForFile(this, ZmanFileProvider.AUTHORITY, new File(imagePath));
        grantUriPermission("com.example.filereceiver", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);// once grant permission
        return uri;
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,REQUESTED_PERMISSIONS,requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_ID: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED ) {
                    finish();
                    break;
                }
                share();
                break;
            }
        }
    }
}
