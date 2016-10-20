package com.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.app.config.ConfigBuile;
import com.app.config.ImageLoader;
import com.bumptech.glide.Glide;
import com.guomin.app.seletcimage.R;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Manager {
    //更多选择
    public void getMoreConfig(Activity context) {
      ConfigBuile.getNewBuile()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setBuileMore()
                .setImageSelectMaximum(9)
                .build(context);


    }

    //单选
    public void getSingleConfig(Activity activity  ) {
        ConfigBuile.getNewBuile()
                .setBuileBar()
                .setActionBarColor(0xfff1f1f1)
                .setStatusBarColor(0xffffffff)
                .setBarOptionHeight(80)
                .setActionBarHeight(450)
                .setBarBackHint("返回")
                .setBarTitleHint("选择图片")
                .setBarOptionHint("完成")
                .setBarBackIconId(R.mipmap.imageselector_back)
                .setBarOptionBackdropId(R.drawable.green_4_bg)
                .complete()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setBuileSingle()
                .build(activity);

    }

    //单选只拍照
    public void getSinglePhotoConfig(Activity activity) {
        ConfigBuile.getNewBuile()
                .setLoading(new ImageShowType())
                .setBuileSingle()
                .setOnlyPhotograph(true)
                .build(activity);

    }

    //单选裁剪
    public void getSingleCropConfig(Activity context) {
         ConfigBuile.getNewBuile()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setBuileSingle()
                .setCrop(true)
                .setAspect(60, 60)
                .setOutput(60, 60)
                 .build(context);

    }

    class ImageShowType implements ImageLoader {

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
