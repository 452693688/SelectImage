package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.app.adapter.ImagesAdapter;
import com.app.adapter.PopupAdapter;
import com.app.bean.ImageBean;
import com.app.bean.ImageFile;
import com.app.config.Configs;
import com.app.imageselect.R;
import com.app.photo.PhotoUtile;
import com.app.view.ActionBar;

import java.io.File;
import java.util.ArrayList;


//选择图片
public class ImageSelectActivity extends AppCompatActivity {

    protected Configs config;
    protected TextView timeTv;
    protected ImagesAdapter iamgesAdapter;
    protected PopupAdapter popupAdapter;
    protected ListPopupWindow fileLv;
    protected View footerRl;
    protected TextView fileNameTv;
    protected ActionBar actionBar;
    //拍照图片路径
    protected File photoFile;
    //裁剪完成之后图片路径
    protected String cropImagePath;
    protected ArrayList<String> paths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        Intent it = getIntent();
        Bundle bundle = null;
        if (it != null) {
            bundle = it.getExtras();
        }
        if (bundle == null) {
            return;
        }
        config = (Configs) bundle.getSerializable("config");
        if (config == null) {
            return;
        }

        init();
    }


    protected void init() {
    }


    //选择文件夹
    protected void onFileClick(int i) {
        ImageFile file = (ImageFile) popupAdapter.getItem(i);
        popupAdapter.setIndex(i);
        fileNameTv.setText(file.getFileName());
        iamgesAdapter.setData(file.imags);
        fileLv.dismiss();
    }

    //选择照片
    protected void onImagesClick(int i) {
        if (i == 0 && config.showCamera) {
            //拍照
            photoFile = PhotoUtile.showCameraAction(this, config);
            return;
        }
        //可以多选择
        if (config.isMore) {
            iamgesAdapter.addORremovePath(i, config.imageSelectMaximum);
            paths = iamgesAdapter.getPaths();
            String hint = config.barOptionHint == null ? "" : config.barOptionHint;
            hint += " ( " + paths.size() + "/" + config.imageSelectMaximum + " ) ";
            actionBar.setOptionText(hint);
            return;
        }
        ImageBean image = (ImageBean) iamgesAdapter.getItem(i);
        //单选+系统裁剪
        if (!config.isMore && config.isCrop && config.isSystemCrop) {
            File file = PhotoUtile.crop(this, config, image.path);
            cropImagePath = file == null ? "" : file.getAbsolutePath();
            return;
        }
        //单选+应用裁剪
        if (!config.isMore && config.isCrop && !config.isSystemCrop) {
            testCrop(image.path);
            return;
        }
        //单选
        if (!config.isMore) {
            paths.add(image.path);
            setResult();
            return;
        }
    }

    //在应用内裁剪
    private final int IMAGE_CROP_IN_APP = 900;
    //完成
    private final int IMAGE_CROP_IN_APP_OK = 901;
    //取消
    private final int IMAGE_CROP_IN_APP_CANCEL = 902;

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照完成
        if (requestCode == PhotoUtile.REQUEST_CAMERA) {
            photoResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == IMAGE_CROP_IN_APP && resultCode == IMAGE_CROP_IN_APP_OK) {
            //应用内裁剪完成
            cropImagePath = data.getStringExtra("arg0");
            paths.add(cropImagePath);
            setResult();
            return;
        }
        if (requestCode == IMAGE_CROP_IN_APP && resultCode == IMAGE_CROP_IN_APP_CANCEL) {
            //应用内裁剪取消
            boolean isOnlyPhotograph = config.onlyPhotograph;
            if (isOnlyPhotograph && !config.isMore) {
                //只拍照 显示拍照
                photoFile = PhotoUtile.showCameraAction(this, config);
                return;
            }
            return;
        }
        //裁剪完成
        if (requestCode == PhotoUtile.REQUEST_CROP) {
            cropResult(requestCode, resultCode, data);
            return;
        }
    }

    //拍照完成
    private void photoResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //BitmapUtile.imageRotate(photoFile);
            //系统裁剪
            if (config.isCrop && config.isSystemCrop) {
                File file = PhotoUtile.crop(this, config, photoFile.getPath());
                cropImagePath = file == null ? "" : file.getAbsolutePath();
                return;
            }
            //应用裁剪
            if (config.isCrop && !config.isSystemCrop) {
                testCrop(photoFile.getPath());
                return;
            }
            //选择更多
            if (config.isMore) {
                setFile(photoFile);
                return;
            }
            paths.add(photoFile.getPath());
            setResult();
            //完成
            return;
        }
        if (photoFile != null && photoFile.exists()) {
            photoFile.delete();
        }
        //只拍照 并且已取消拍照
        if (config.onlyPhotograph) {
            setResult();
            return;
        }
    }

    protected void setFile(File imageFile) {
    }

    //裁剪完成
    private void cropResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        paths.add(cropImagePath);
        setResult();
    }

    //应用裁剪
    protected void testCrop(String path) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        Intent it = new Intent();
        it.putExtra("arg0", path);
        it.putExtra("arg1", width);
        it.putExtra("arg2", height);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", config);
        it.putExtras(bundle);
        it.setClass(this, CropActivity.class);
        startActivityForResult(it, IMAGE_CROP_IN_APP);
    }

    //获取图片完成
    protected void setResult() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Configs.TASK_COMPLETE_RESULT, paths);
        setResult(Configs.TASK_COMPLETE, intent);
        finish();
    }

}
