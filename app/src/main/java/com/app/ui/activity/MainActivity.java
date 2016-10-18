package com.app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.guomin.app.seletcimage.R;

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

    private PhotoManager photo;
    Manager manager = new Manager();

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

            case R.id.image_btn:
                if (photo == null) {
                    photo = new PhotoManager(this);
                }
                photo.corpSquare();
                break;
        }

    }
}
