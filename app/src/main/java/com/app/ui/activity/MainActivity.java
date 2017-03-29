package com.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.config.Configs;
import com.app.config.entity.ImageEntity;
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
        findViewById(R.id.image_btn9).setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.image_iv);
    }

    private ArrayList<String> getPaths() {
        ArrayList<String> paths = new ArrayList<>();
        paths.add("/storage/emulated/0/temp/pictures/20170302145937.png");
        paths.add("/storage/emulated/0/temp/pictures/20170302144916.png");
        paths.add("/storage/emulated/0/temp/pictures/20170302145653..png");
        paths.add("/storage/emulated/0/temp/pictures/20170302145704..png");
        return paths;
    }

    PhotoManager photoManager;

    @Override
    public void onClick(View view) {
        if (photoManager == null) {
            photoManager = new PhotoManager(this);
        }
        switch (view.getId()) {
            case R.id.image_btn2:
                //只预览
                photoManager.previewImage(getPaths());
                break;
            case R.id.image_btn9:
                //预览和删除
                photoManager.previewImageDelect(getPaths());
                break;
            case R.id.image_btn3:
                //单张图片+裁剪
                photoManager.getCrop(true);
                break;
            case R.id.image_btn4:
                //只拍照
                photoManager.getSinglePhotoConfig();
                break;
            case R.id.image_btn5:
                //多选
                photoManager.getMoreConfig(8, getPaths());
                break;
            case R.id.image_btn6:
                //选图+系统裁剪
                photoManager.getCrop(false);
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
                Bundle bundle = data.getExtras();
                ArrayList<ImageEntity> image = (ArrayList<ImageEntity>) bundle.get(Configs.TASK_COMPLETE_RESULT);
                Glide.with(this)
                        .load(image.get(0).imagePath)
                        .placeholder(R.mipmap.image_select_default)
                        .centerCrop()
                        .into(iv);
                break;
            case Configs.TASK_PICTURE_COMPLETE:
                bundle = data.getExtras();
                image = (ArrayList<ImageEntity>) bundle.get(Configs.TASK_COMPLETE_RESULT);
                for (int i = 0; i < image.size(); i++) {
                    Log.e("====", image.get(i).imagePath);
                }
                String path = image.get(0).imagePath;
                Bitmap bit = ImageUtile.getSmallBitmap(path);
                iv.setImageBitmap(bit);
                break;
            case Configs.TASK_PRIVATE_DELECTE:
                bundle = data.getExtras();
                image = (ArrayList<ImageEntity>) bundle.get(Configs.TASK_COMPLETE_RESULT);
                for (int i = 0; i < image.size(); i++) {
                    Log.e("====", image.get(i).imagePath);
                }
                break;
        }
    }

}
