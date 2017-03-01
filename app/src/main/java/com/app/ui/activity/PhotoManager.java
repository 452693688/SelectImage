package com.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.app.config.ConfigBuile;
import com.app.config.Configs;
import com.app.config.ImageLoader;
import com.bumptech.glide.Glide;
import com.guomin.app.seletcimage.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class PhotoManager {
    private Activity activity;
    private int barHeight;

    public PhotoManager(Activity activity) {
        this.activity = activity;
        barHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                42, activity.getResources().getDisplayMetrics());
    }


    //更多选择
    public void getMoreConfig() {
        ConfigBuile.getNewBuile()
                .setBuileBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .setBarOptionHeight(60)
                .setBarBackHint("返回")
                .setBarTitleHint("选择图片")
                .setBarOptionHint("完成")
                .setBarOptionBackdropId(R.drawable.green_4_bg)
                .complete()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setBuileMore()
                .setImageSelectMaximum(3)
                .build(activity);
    }


    //单选
    public void getSingleConfig() {

        ConfigBuile.getNewBuile()
                .setBuileBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .setBarBackHint("返回")
                .setBarTitleHint("选择图片")
                .complete()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setBuileSingle()
                .build(activity);

    }

    //单选只拍照
    public void getSinglePhotoConfig() {

        ConfigBuile.getNewBuile()
                .setLoading(new ImageShowType())
                .setBuileSingle()
                .setOnlyPhotograph(true)
                .build(activity);

    }

    //单选裁剪
    public void getSingleCropConfig() {
        ConfigBuile.getNewBuile()
                .setBuileBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .setBarBackHint("返回")
                .setBarTitleHint("选择图片")
                .setBarCorpTitleHint("裁剪")
                .setBarOptionHint("完成")
                .complete()
                .setLoading(new ImageShowType())
                .setShowCamera(true)
                .setBuileSingle()
                .setCrop(true)
                .setSystemCrop(false)
                .setAspect(600, 600)
                .setOutput(600, 600)
                .build(activity);

    }

    //单选只拍照+裁剪
    public void getSinglePhotoCropConfig() {
        ConfigBuile.getNewBuile()
                .setBuileBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .setBarBackHint("返回")
                .setBarTitleHint("选择图片")
                .setBarCorpTitleHint("裁剪")
                .setBarOptionHint("完成")
                .complete()
                .setLoading(new ImageShowType())
                .setBuileSingle()
                .setCrop(true)
                .setSystemCrop(false)
                .setAspect(600, 600)
                .setOutput(600, 600)
                .setOnlyPhotograph(true)
                .build(activity);

    }

    class ImageShowType implements ImageLoader {

        @Override
        public void imageLoading(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.image_select_default)
                    .centerCrop()
                    .into(imageView);
        }
    }

    public List<String> onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Configs.TASK_COMPLETE) {
            return null;
        }
        ArrayList<String> pathList = data.getStringArrayListExtra(Configs.TASK_COMPLETE_RESULT);
        if (pathList == null || pathList.size() == 0) {
            return null;
        }
        for (String path : pathList) {
            Log.e("ImagePathList", path);
        }
        return pathList;
    }

}
