package com.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.app.config.Configs;
import com.guomin.app.seletcimage.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.image_btn2).setOnClickListener(this);
        findViewById(R.id.image_btn3).setOnClickListener(this);
        findViewById(R.id.image_btn4).setOnClickListener(this);
        findViewById(R.id.image_btn5).setOnClickListener(this);
    }

    PhotoManager photoManager;

    @Override
    public void onClick(View view) {
        if (photoManager == null) {
            photoManager = new PhotoManager(this);
        }
        switch (view.getId()) {
            case R.id.image_btn2:
                //单张图片
                photoManager.getSingleConfig();
                break;
            case R.id.image_btn3:
                //单张图片+裁剪
                photoManager.getSingleCropConfig();
                break;
            case R.id.image_btn4:
                //只拍照
                photoManager.getSinglePhotoConfig();
                break;
            case R.id.image_btn5:
                //多选
                photoManager.getMoreConfig();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Configs.TASK_COMPLETE) {
            ArrayList<String> pathList = data.getStringArrayListExtra(Configs.TASK_COMPLETE_RESULT);
            if (pathList == null) {
                return;
            }
            for (String path : pathList) {
                Log.e("ImagePathList", path);
            }
        }
    }
}
