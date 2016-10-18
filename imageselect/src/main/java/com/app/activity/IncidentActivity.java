package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.Toast;

import com.app.adapter.PopupAdapter;
import com.app.bean.ImageBean;
import com.app.bean.ImageFile;
import com.app.config.Configs;
import com.app.imageselect.R;
import com.app.utils.ImageMnager;
import com.app.utils.ImageUtile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IncidentActivity extends ImageSelectActivity {

















    //
    @Override
    protected void incident() {
        boolean isOnlyPhotograph = config.isOnlyPhotograph();
        if (isOnlyPhotograph && !config.isMore()) {
            //只拍照
            photoFile = ImageUtile.showCameraAction(this,config);
            return;
        }
        doRequest();
    }

    private ImageMnager imageMnager;
    private LoadingListener loadingListener = new LoadingListener();

    private void doRequest() {
        if (imageMnager == null) {
            imageMnager = new ImageMnager(this, config.isShowCamera());
            imageMnager.setOnLoadingListener(loadingListener);
        }
        getSupportLoaderManager().initLoader(ImageMnager.LOADER_ALL, null, imageMnager);

    }

    private List<ImageFile> imageFils;

    //读取数据库照片监听
    class LoadingListener implements ImageMnager.OnLoadingListener {

        @Override
        public void onLoadingListener(List<ImageFile> fils) {
            findViewById(R.id.progress_rl).setVisibility(View.GONE);
            if (fils == null) {
                Toast.makeText(IncidentActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            ImageFile file = fils.get(0);
            if (fils.size() == 1) {
                //不显示选择文件夹
                footerRl.setVisibility(View.GONE);
            }
            imageFils = fils;
            iamgesAdapter.setData(file.imags);
        }
    }

    //多选拍照之后...
    public void setFile(File imageFile) {
        String path = imageFile.getPath();
        String name = imageFile.getName();
        File parentFile = imageFile.getParentFile();
        String filePath = parentFile.getAbsolutePath();
        String fileName = parentFile.getName();
        //
        ImageBean image = new ImageBean(path, filePath, fileName, name, 0);
        boolean isAdd = false;
        for (int i = 0; i < imageFils.size(); i++) {
            ImageFile file = imageFils.get(i);
            if (i == 0) {
                file.imags.add(1, image);
                continue;
            }
            String absolutePath = file.getFilePath();
            if (absolutePath.equals(filePath)) {
                //是同一个文件夹
                file.imags.add(1, image);
                isAdd = true;
                break;
            }
        }
        if (!isAdd) {
            ImageFile file = imageMnager.getFristBean(new ArrayList<ImageBean>(), 1);
            file.imags.add(1, image);
            imageFils.add(file);
        }
        loadingListener.onLoadingListener(imageFils);
    }

    @Override
    public void onClick(View view) {
        if (fileLv == null) {
            fileLv = new ListPopupWindow(this);
            popupAdapter = new PopupAdapter();
            fileLv.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            fileLv.setAdapter(popupAdapter);
            int width = footerRl.getWidth();
            fileLv.setContentWidth(width);
            fileLv.setWidth(width);
            fileLv.setHeight((int) (width * 0.8));
            fileLv.setAnchorView(footerRl);
            fileLv.setModal(true);
            fileLv.setOnItemClickListener(this);
            popupAdapter.setData(imageFils);
        }
        boolean isShow = fileLv.isShowing();
        if (isShow) {
            fileLv.dismiss();
        } else {
            fileLv.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != ImageUtile.REQUEST_CAMERA) {
            photoResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode != ImageUtile.REQUEST_CROP) {
            cropResult(requestCode, resultCode, data);
            return;
        }


    }

    private ArrayList<String> paths = new ArrayList<>();

    //拍照完成
    private void photoResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //裁剪
            if (config.isCrop()) {
                File file = ImageUtile.crop(this, config, photoFile.getPath());
                cropImagePath = file == null ? "": file.getAbsolutePath();
                return;
            }
            //选择更多
            if (config.isMore()) {
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
        intent.putStringArrayListExtra("data", paths);
        setResult(Configs.TASK_COMPLETE, intent);
        finish();
    }

    @Override
    protected void setResult(String path) {
        paths.add(path);
        setResult();
    }
}
