package com.example.wxsharedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wxsharedemo.activity.ScopedStorageActivity;
import com.example.wxsharedemo.activity.ShareActivity;
import com.example.wxsharedemo.activity.SharedStorageMediaStoreActivity;
import com.example.wxsharedemo.activity.SharedStorageSAFActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt_share, R.id.bt_scoped_storage, R.id.bt_shared_storage_ms, R.id.bt_shared_storage_saf})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_share:
                startActivity(new Intent(this, ShareActivity.class));
                break;
            case R.id.bt_scoped_storage:
                startActivity(new Intent(this, ScopedStorageActivity.class));
                break;
            case R.id.bt_shared_storage_ms:
                startActivity(new Intent(this, SharedStorageMediaStoreActivity.class));
                break;
            case R.id.bt_shared_storage_saf:
                startActivity(new Intent(this, SharedStorageSAFActivity.class));
                break;
        }
    }

}