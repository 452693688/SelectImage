package com.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.activity.IncidentActivity;
import com.app.config.ConfigBuile;
import com.app.config.Configs;
import com.bumptech.glide.Glide;
import com.guomin.app.seletcimage.R;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Manager {
    //更多选择
    public void getMoreConfig(Context context) {
        Configs build = ConfigBuile.getBuile()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setMore()
                .setImageSelectMaximum(9)
                .builds();

        Intent it = new Intent(context, IncidentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", build);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    //单选
    public void getSingleConfig(Context context) {
        Configs build = ConfigBuile.getBuile()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setSingle()
                .build();
        Intent it = new Intent(context, IncidentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", build);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    //单选只拍照
    public void getSinglePhotoConfig(Context context) {
        Configs build = ConfigBuile.getBuile()
                .setLoading(new ImageShowType())
                .setSingle()
                .setOnlyPhotograph(true)
                .build();
        Intent it = new Intent(context, IncidentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", build);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    //单选裁剪
    public void getSingleCropConfig(Context context) {
        Configs build = ConfigBuile.getBuile()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setSingle()
                .setCrop(true)
                .setAspect(60, 60)
                .setOutput(60, 60)
                .build();
        Intent it = new Intent(context, IncidentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", build);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    class ImageShowType implements ConfigBuile.ImageLoader {

        @Override
        public void imageLoading(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }
    }
}
