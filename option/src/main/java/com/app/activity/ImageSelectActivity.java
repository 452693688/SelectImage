package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.app.adapter.ImagesAdapter;
import com.app.adapter.PopupAdapter;
import com.app.bean.ImageBean;
import com.app.bean.ImageFile;
import com.app.config.Configs;
import com.app.imageselect.R;
import com.app.photo.DataStore;
import com.app.photo.PhotoUtile;
import com.app.unmix.DLog;
import com.app.view.ActionBar;

import java.io.File;
import java.util.ArrayList;


//选择图片
public class ImageSelectActivity extends AppCompatActivity {
    private String TAG = "ImageSelectActivity";
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
        DLog.setIsDebug(config.isDebug);
        init(it);
    }


    protected void init(Intent it) {
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
        if (!config.isMore && config.isCrop && !config.isNotSystemCrop) {
            File file = PhotoUtile.crop(this, config, image.path);
            cropImagePath = file == null ? "" : file.getAbsolutePath();
            return;
        }
        //单选+应用裁剪
        if (!config.isMore && config.isCrop && config.isNotSystemCrop) {
            appInCrop(image.path);
            return;
        }
        //单选
        if (!config.isMore) {
            paths.add(image.path);
            setResultIntent(Configs.TASK_PICTURE_COMPLETE);
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
        DLog.e(TAG, "onActivityResult");
        //拍照完成
        if (requestCode == PhotoUtile.REQUEST_CAMERA) {
            photoResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == IMAGE_CROP_IN_APP && resultCode == IMAGE_CROP_IN_APP_OK) {
            //应用内裁剪完成
            cropImagePath = data.getStringExtra("arg0");
            paths.add(cropImagePath);
            setResultIntent(Configs.TASK_CROP_COMPLETE);
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

    private String getPhotoPath() {
        boolean isExit = photoFile.exists();
        String photoPath = photoFile.getAbsolutePath();
        String msg = isExit ? "存在" : "不存在";
        DLog.e(TAG, "拍照path：" + photoPath + msg);
        if (!isExit) {
            //照片路径不存在
            photoPath = DataStore.stringGet(this, DataStore.PATH_TAKE);
            photoFile = new File(photoPath);
        }
        isExit = photoFile.exists();
        if (!isExit) {
            photoPath = "";
        }
        msg = isExit ? "存在" : "不存在";
        DLog.e(TAG, "拍照path：" + photoPath + msg);
        return photoPath;
    }

    //拍照完成
    private void photoResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String photoPath = getPhotoPath();
            if (TextUtils.isEmpty(photoPath)) {
                //照片路径不存在
                return;
            }
            //系统裁剪
            if (config.isCrop && !config.isNotSystemCrop) {
                File file = PhotoUtile.crop(this, config, photoPath);
                cropImagePath = file == null ? "" : file.getAbsolutePath();
                return;
            }
            //应用裁剪
            if (config.isCrop && config.isNotSystemCrop) {
                appInCrop(photoPath);
                return;
            }
            //选择更多
            if (config.isMore) {
                setFile(photoFile);
                return;
            }
            paths.add(photoPath);
            setResultIntent(Configs.TASK_PICTURE_COMPLETE);
            //完成
            return;
        }
        if (photoFile != null && photoFile.exists()) {
            photoFile.delete();
        }
        //只拍照 并且已取消拍照
        if (config.onlyPhotograph) {
            DLog.e(TAG, "取消拍照");
            setResultIntent(Configs.TASK_CANCEL);
            return;
        }
    }

    protected void setFile(File imageFile) {
    }

    //裁剪完成
    private void cropResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            //放弃裁剪   显示拍照
            DLog.e(TAG, "裁剪失败");
            DataStore.stringSave(this, DataStore.PATH_CROP, "");
            photoFile = PhotoUtile.showCameraAction(this, config);
            return;
        }
        DLog.e(TAG, "裁剪完成：" + cropImagePath);
        if (TextUtils.isEmpty(cropImagePath)) {
            cropImagePath = DataStore.stringGet(this, DataStore.PATH_CROP);
            DLog.e(TAG, "裁剪完成：path重新获取" + cropImagePath);
        }
        paths.add(cropImagePath);
        setResultIntent(Configs.TASK_CROP_COMPLETE);
    }

    //应用内裁剪
    protected void appInCrop(String path) {
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
    protected void setResultIntent(int resultCode) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Configs.TASK_COMPLETE_RESULT, paths);
        setResult(resultCode, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResultIntent(Configs.TASK_CANCEL);
    }
}
