package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.TextView;

import com.app.adapter.ImagesAdapter;
import com.app.adapter.PopupAdapter;
import com.app.bean.ImageBean;
import com.app.bean.ImageFile;
import com.app.config.Configs;
import com.app.imageselect.R;
import com.app.utils.ImageUtile;
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
            photoFile = ImageUtile.showCameraAction(this, config);
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
        //单选+裁剪
        if (!config.isMore && config.isCrop) {
            File file = ImageUtile.crop(this, config, image.path);
            cropImagePath = file == null ? "" : file.getAbsolutePath();
            return;
        }
        //单选
        if (!config.isMore) {
            paths.add(image.path);
            setResult();
            return;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageUtile.REQUEST_CAMERA) {
            photoResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == ImageUtile.REQUEST_CROP) {
            cropResult(requestCode, resultCode, data);
            return;
        }


    }

    //拍照完成
    private void photoResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //裁剪
            if (config.isCrop) {
                File file = ImageUtile.crop(this, config, photoFile.getPath());
                cropImagePath = file == null ? "" : file.getAbsolutePath();
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

    protected void setResult() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Configs.TASK_COMPLETE_RESULT, paths);
        setResult(Configs.TASK_COMPLETE, intent);
        finish();
    }


}
