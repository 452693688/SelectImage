package com.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.ImagesAdapter;
import com.app.adapter.PopupAdapter;
import com.app.bean.ImageBean;
import com.app.bean.ImageFile;
import com.app.config.Configs;
import com.app.imageselect.R;
import com.app.photo.DataStore;
import com.app.photo.DateUtile;
import com.app.photo.PhotoUtile;
import com.app.photo.PhotosMnager;
import com.app.unmix.DLog;
import com.app.view.ActionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IncidentActivity extends ImageSelectActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PhotosMnager imageMnager;
    private LoadingListener loadingListener = new LoadingListener();
    private String TAG = "IncidentActivity";

    protected void init(Intent it) {
        DLog.e(TAG, "onCreate");
        boolean isOnlyPhotograph = config.onlyPhotograph;
        if (isOnlyPhotograph && !config.isMore) {
            //只拍照
            String photoPath = DataStore.stringGet(this, DataStore.PATH_TAKE);
            if (!TextUtils.isEmpty(photoPath)) {
                photoFile = new File(photoPath);
                return;
            }
            photoFile = PhotoUtile.showCameraAction(this, config);
            return;
        }
        //
        GridView iamgeGv = (GridView) findViewById(R.id.image_gv);
        iamgesAdapter = new ImagesAdapter(this, config.isMore, config.showCamera);
        iamgesAdapter.setItemSize(this);
        iamgeGv.setAdapter(iamgesAdapter);
        timeTv = (TextView) findViewById(R.id.time_tv);
        fileNameTv = (TextView) findViewById(R.id.file_name_tv);
        fileNameTv.setOnClickListener(this);
        footerRl = findViewById(R.id.footer_rl);
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setConfigs(this, config, this);
        //
        iamgeGv.setOnScrollListener(new SlideListener());
        iamgeGv.setOnItemClickListener(this);
        doRequest();
    }

    private void doRequest() {
        if (imageMnager == null) {
            imageMnager = new PhotosMnager(this, config.showCamera);
            imageMnager.setOnLoadingListener(loadingListener);
        }
        getSupportLoaderManager().initLoader(PhotosMnager.LOADER_ALL, null, imageMnager);

    }

    //照片选择监听
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.image_gv) {
            onImagesClick(i);
        } else {
            onFileClick(i);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bra_option) {
            //更多发送
            setResultIntent(Configs.TASK_PICTURE_COMPLETE);
            return;
        }
        if (id == R.id.bra_back) {
            //返回
            onBackPressed();
            return;
        }
        if (id == R.id.file_name_tv) {
            //呼出照片文件夹选择
            popupWindow();
        }

    }

    private void popupWindow() {
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

    //多选拍照之后...
    protected void setFile(File imageFile) {
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


    private List<ImageFile> imageFils;

    //读取数据库照片监听
    class LoadingListener implements PhotosMnager.OnLoadingListener {

        @Override
        public void onLoadingListener(List<ImageFile> fils) {
            findViewById(R.id.progress_il).setVisibility(View.GONE);
            if (fils == null) {
                Toast.makeText(IncidentActivity.this, R.string.image_loading_error, Toast.LENGTH_SHORT).show();
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

    class SlideListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                timeTv.setVisibility(View.GONE);
            } else if (scrollState == SCROLL_STATE_FLING) {
                timeTv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (timeTv.getVisibility() == View.VISIBLE) {
                int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                ImageBean image = (ImageBean) view.getAdapter().getItem(index);
                if (image != null) {
                    timeTv.setText(DateUtile.formatPhotoDate(image.path));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DLog.e(TAG, "onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            DLog.e(TAG, "onConfigurationChanged=竖屏");
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DLog.e(TAG, "onConfigurationChanged=横屏");
        }
    }
}
