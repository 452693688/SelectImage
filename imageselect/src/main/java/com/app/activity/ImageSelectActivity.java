package com.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.app.utils.DateUtile;
import com.app.utils.FileUtile;

import java.io.File;


//选择图片
public class ImageSelectActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemClickListener {

    protected Configs config;
    protected TextView timeTv;
    protected ImagesAdapter iamgesAdapter;
    protected PopupAdapter popupAdapter;
    protected ListPopupWindow fileLv;
    protected View footerRl;
    protected Button fileBtn;


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
        iamgesAdapter = new ImagesAdapter(this);
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
                        timeTv.setText(DateUtile.formatPhotoDate(image.path));
                    }
                }
            }
        });
        iamgeGv.setOnItemClickListener(this);
        incident();
    }

    protected void incident() {
    }


    @Override
    public void onClick(View view) {
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

    //选择文件夹
    private void onFileClick(int i) {
        ImageFile file = (ImageFile) popupAdapter.getItem(i);
        popupAdapter.setIndex(i);
        fileBtn.setText(file.getFileName());
        iamgesAdapter.setData(file.imags);
        fileLv.dismiss();
    }

    //选择照片
    private void onImagesClick(int i) {
        if (i == 0) {
            //拍照
            return;
        }
        iamgesAdapter.getItem(i);
        //可以多选择
        if (config.isMore()) {
            iamgesAdapter.addORremovePath(i, config.getImageSelectMaximum());
            return;
        }
        //单选+裁剪
        if (!config.isMore() && config.isCrop()) {
            return;
        }
        //单选
        if (!config.isMore()) {
            return;
        }
    }

    protected File tempFile;
    //拍照
    protected static final int REQUEST_CAMERA = 100;
    //裁剪
    protected static final int REQUEST_CROP = 101;
    //  选择相机
    protected void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            tempFile = FileUtile.createTmpFile(this, config.getFilePath());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, "No system camera found", Toast.LENGTH_SHORT).show();
        }
    }

    protected String cropImagePath;

    //裁剪
    protected void crop(String imagePath) {
        File file;
        if (FileUtile.existSDCard()) {
            file = new File(Environment.getExternalStorageDirectory() + config.getFilePath(),
                    FileUtile.getImageName());
        } else {
            file = new File(getCacheDir(), FileUtile.getImageName());
        }
        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.getAspectX());
        intent.putExtra("aspectY", config.getAspectY());
        intent.putExtra("outputX", config.getOutputX());
        intent.putExtra("outputY", config.getOutputY());
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CROP);
    }
}
