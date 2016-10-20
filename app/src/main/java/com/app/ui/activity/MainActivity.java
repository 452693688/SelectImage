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
        findViewById(R.id.image_btn).setOnClickListener(this);
        findViewById(R.id.image_btn2).setOnClickListener(this);
        findViewById(R.id.image_btn3).setOnClickListener(this);
        findViewById(R.id.image_btn4).setOnClickListener(this);
        findViewById(R.id.image_btn5).setOnClickListener(this);
    }

    ImageSelectManager manager = new ImageSelectManager();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_btn2:
                //单张图片
                manager.getSingleConfig(this);
                break;
            case R.id.image_btn3:
                //单张图片+裁剪
                manager.getSingleCropConfig(this);
                break;
            case R.id.image_btn4:
                //只拍照
                manager.getSinglePhotoConfig(this);
                break;
            case R.id.image_btn5:
                //多选
                manager.getMoreConfig(this);
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
