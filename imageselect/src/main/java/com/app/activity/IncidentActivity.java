package com.app.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.ListPopupWindow;
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
import com.app.imageselect.R;
import com.app.utils.DateUtile;
import com.app.utils.ImageMnager;
import com.app.utils.ImageUtile;
import com.app.view.ActionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IncidentActivity extends ImageSelectActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageMnager imageMnager;
    private LoadingListener loadingListener = new LoadingListener();


    protected void init() {
        boolean isOnlyPhotograph = config.onlyPhotograph;
        if (isOnlyPhotograph && !config.isMore) {
            //只拍照
            photoFile = ImageUtile.showCameraAction(this, config);
            return;
        }
        //
        GridView iamgeGv = (GridView) findViewById(R.id.image_gv);
        iamgesAdapter = new ImagesAdapter(this, config.isMore);
        iamgesAdapter.setItemSize(this);
        iamgeGv.setAdapter(iamgesAdapter);
        timeTv = (TextView) findViewById(R.id.time_tv);
        fileNameTv = (TextView) findViewById(R.id.file_name_tv);
        fileNameTv.setOnClickListener(this);
        footerRl = findViewById(R.id.footer_rl);
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setConfigs(this, config);
        actionBar.barOptionTv.setOnClickListener(this);
        //
        iamgeGv.setOnScrollListener(new SlideListener());
        iamgeGv.setOnItemClickListener(this);
        doRequest();
    }

    private void doRequest() {
        if (imageMnager == null) {
            imageMnager = new ImageMnager(this, config.showCamera);
            imageMnager.setOnLoadingListener(loadingListener);
        }
        getSupportLoaderManager().initLoader(ImageMnager.LOADER_ALL, null, imageMnager);

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

}
