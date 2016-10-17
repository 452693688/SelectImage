package com.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guomin.app.seletcimage.R;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class PhotoManager {
    private final ImageConfig.Builder imageConfig;
    private Activity activity;
    private Resources res;

    public PhotoManager(Activity activity) {
        this.activity = activity;
        res = activity.getResources();
        imageConfig
                = new ImageConfig.Builder(activity, new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(res.getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(res.getColor(R.color.blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(res.getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(res.getColor(R.color.white));
    }

    //选择多张图片
    public void selecMmore() {

        imageConfig
                .pathList(new ArrayList<String>())
                .mutiSelect()
                .mutiSelectMaxSize(9)
                .showCamera()
                // 已选择的图片路径
                // .pathList(path)
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());   // 开启图片选择器
    }

    //选择一张图片
    public void selectOne() {
        imageConfig
                .pathList(new ArrayList<String>())
                .singleSelect()
                .showCamera()
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());
    }

    //选择完图片要进行裁剪
    public void corpSquare() {

        imageConfig
                .pathList(new ArrayList<String>())
                .crop()
                .singleSelect()
                .showCamera()
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());
    }

    //单选自定义截图
    public void corpFreedom() {

        imageConfig
                .pathList(new ArrayList<String>())
                .crop(1, 2, 500, 1000)
                .singleSelect()
                .showCamera()
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());
    }

    public class GlideLoader implements com.yancy.imageselector.ImageLoader {

        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }

    }


    public List<String> onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> pathList = null;
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                 Log.e("ImagePathList", path);
            }
        }
        return pathList;
    }
}
