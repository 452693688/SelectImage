package com.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.ImagesAdapter;
import com.app.adapter.PopupAdapter;
import com.app.bean.ImageBean;
import com.app.bean.ImageFile;
import com.app.config.Configs;
import com.app.imageselect.R;
import com.app.utils.DateUtils;
import com.app.utils.ImageMnager;

import java.util.List;


//选择图片
public class ImageSelectActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemClickListener {

    private Configs config;
    private TextView timeTv;
    private ImagesAdapter iamgesAdapter;
    private PopupAdapter popupAdapter;
    private ListPopupWindow fileLv;
    private View footerRl;
    private Button fileBtn;

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

    private void init() {
        GridView iamgeGv = (GridView) findViewById(R.id.image_gv);
        iamgesAdapter = new ImagesAdapter();
        iamgesAdapter.setItemSize(this);
        iamgeGv.setAdapter(iamgesAdapter);
        timeTv = (TextView) findViewById(R.id.time_tv);
        fileBtn = (Button) findViewById(R.id.file_btn);
        fileBtn.setOnClickListener(this);
        footerRl = findViewById(R.id.footer_rl);
        //
        iamgeGv.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        timeTv.setText(DateUtils.formatPhotoDate(image.path));
                    }
                }
            }
        });
        //
        doRequest();
    }


    private ImageMnager imageMnager;

    private void doRequest() {
        if (imageMnager == null) {
            imageMnager = new ImageMnager(this,config.isShowCamera());
            imageMnager.setOnLoadingListener(new LoadingListener());
        }
        getSupportLoaderManager().initLoader(ImageMnager.LOADER_ALL, null, imageMnager);

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

    //照片选择监听
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            //拍照
            return;
        }
        ImageFile file = (ImageFile) popupAdapter.getItem(i);
        popupAdapter.setIndex(i);
        fileBtn.setText(file.getFileName());
        iamgesAdapter.setData(file.imags);
        if (fileLv != null) {
            fileLv.dismiss();
        }
    }

    private List<ImageFile> imageFils;

    //读取数据库照片监听
    class LoadingListener implements ImageMnager.OnLoadingListener {

        @Override
        public void onLoadingListener(List<ImageFile> fils) {
            findViewById(R.id.progress_rl).setVisibility(View.GONE);
            if (fils == null) {
                Toast.makeText(ImageSelectActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
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
}
