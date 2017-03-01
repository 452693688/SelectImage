package com.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.app.config.Configs;
import com.app.crop.EnjoyCropLayout;
import com.app.crop.core.BaseLayerView;
import com.app.crop.core.clippath.ClipPathLayerView;
import com.app.crop.core.clippath.ClipPathSquare;
import com.app.crop.core.mask.ColorMask;
import com.app.imageselect.R;
import com.app.photo.FileUtile;
import com.app.unmix.BitmapUtile;
import com.app.unmix.DLog;
import com.app.view.ActionBar;

import java.io.File;


//裁剪图片
public class CropActivity extends AppCompatActivity implements View.OnClickListener {
    private EnjoyCropLayout enjoyCropLayout;
    private Configs config;
    private String path;
    private int width, height;
    private LinearLayout contentLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Intent it = getIntent();
        // 屏幕宽度（像素）
        Bundle bundle = null;
        if (it != null) {
            path = it.getStringExtra("arg0");
            width = it.getIntExtra("arg1", 0);
            height = it.getIntExtra("arg2", 0);
            bundle = it.getExtras();
            DLog.e("CropActivity", "图片路径：" + path);
        }

        if (bundle == null) {
            DLog.e("CropActivity", "bundle：读取失败");
            onBackPressed();
            return;
        }
        config = (Configs) bundle.getSerializable("config");
        if (config == null) {
            DLog.e("CropActivity", "config：读取失败");
            onBackPressed();
            return;
        }
        if (TextUtils.isEmpty(path)) {
            DLog.e("CropActivity", "path：读取失败");
            onBackPressed();
            return;
        }
        ActionBar cctionBar = (ActionBar) findViewById(R.id.actionbar);
        cctionBar.setConfigsCrop(this, config, this);
        if (width > height) {
            int temp = height;
            width = height;
            height = temp;
        }
        contentLl = (LinearLayout) findViewById(R.id.content_ll);
        viewResumeHandler.sendEmptyMessageDelayed(1, 500);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bra_option) {
            Bitmap bitmap = enjoyCropLayout.crop();
            File file = FileUtile.createPhotoFile(this, config.filePath);
            boolean isSave = BitmapUtile.saveBitmaps(bitmap, file);
            if (!isSave) {
                DLog.e("", "保存失败");
                return;
            }
            Intent it = new Intent();
            it.putExtra("arg0", file.getAbsolutePath());
            setResult(901, it);
            finish();
            return;
        }
        if (id == R.id.bra_back) {
            onBackPressed();
            return;
        }

    }


    @Override
    public void onBackPressed() {
        setResult(902);
        super.onBackPressed();
    }

    private void initContent() {
        //enjoyCropLayout = (EnjoyCropLayout) findViewById(R.id.cl);
        enjoyCropLayout = new EnjoyCropLayout(this);
        contentLl.addView(enjoyCropLayout);
        //设置裁剪原图片
        Bitmap bitmap = BitmapUtile.resizeBitmap(path, width, height);
        if (bitmap == null) {
            DLog.e("CropActivity", "bitmap：读取失败");
            onBackPressed();
            return;
        }
        //旋转图片
        Bitmap bitmapRotate = BitmapUtile.imageRotate(path, bitmap);
        // Bitmap imageBitmap = BitmapFactory.decodeFile(path);
        // enjoyCropLayout.setImageResource(R.drawable.test2);
        enjoyCropLayout.setImage(bitmapRotate);
        defineCropParams();
        findViewById(R.id.progress_il).setVisibility(View.GONE);
    }

    private void defineCropParams() {
        int aspectX = config.aspectX;
        if (aspectX <= 0) {
            aspectX = 100;
        }
        //设置裁剪集成视图，这里通过一定的方式集成了遮罩层与预览框
        BaseLayerView layerView = new ClipPathLayerView(this);
        //设置遮罩层,这里使用半透明的遮罩层
        layerView.setMask(ColorMask.getTranslucentMask());
        //设置预览框形状
        // layerView.setShape(new ClipPathCircle(aspectX));
        layerView.setShape(new ClipPathSquare(aspectX));
        //设置裁剪集成视图
        enjoyCropLayout.setLayerView(layerView);
        //设置边界限制，如果设置了该参数，预览框则不会超出图片
        enjoyCropLayout.setRestrict(true);
    }

    private ViewResumeHandler viewResumeHandler = new ViewResumeHandler();

    class ViewResumeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initContent();
        }
    }


}
