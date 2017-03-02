package com.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.config.Configs;
import com.bumptech.glide.Glide;
import com.guomin.app.seletcimage.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.image_btn2).setOnClickListener(this);
        findViewById(R.id.image_btn3).setOnClickListener(this);
        findViewById(R.id.image_btn4).setOnClickListener(this);
        findViewById(R.id.image_btn5).setOnClickListener(this);
        findViewById(R.id.image_btn6).setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.image_iv);
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
            case R.id.image_btn6:
                //单选只拍照+裁剪
                photoManager.getSinglePhotoCropConfig();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Configs.TASK_START) {
            return;
        }
        switch (resultCode) {
            case Configs.TASK_CANCEL:
                break;
            case Configs.TASK_CROP_COMPLETE:
                ArrayList<String> pathList = data.getStringArrayListExtra(Configs.TASK_COMPLETE_RESULT);
                Glide.with(this)
                        .load(pathList.get(0))
                        .placeholder(R.mipmap.image_select_default)
                        .centerCrop()
                        .into(iv);
                break;
            case Configs.TASK_PICTURE_COMPLETE:
                pathList = data.getStringArrayListExtra(Configs.TASK_COMPLETE_RESULT);
                String path = pathList.get(0);
                Bitmap bit = ImageUtile.getSmallBitmap(path);
                iv.setImageBitmap(bit);
                break;
        }
    }

}
