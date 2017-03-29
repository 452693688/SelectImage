package com.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.config.Configs;
import com.app.imageselect.R;
import com.app.ui.adapter.PreviewAdapter;
import com.app.config.entity.ImageEntity;
import com.app.ui.view.ActionBar;
import com.app.ui.view.ViewPagerFixed;

import java.util.ArrayList;


public class PreviewOnlyActivity extends PreviewActivity {
    private PreviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle == null) {
            return;
        }
        config = (Configs) bundle.getSerializable("config");

        if (config == null) {
            return;
        }
        isOnlyShow = true;
        //
        bottomRl = findViewById(R.id.bottom_rl);
        rootView = findViewById(R.id.content);
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        findViewById(R.id.preview_choose_tv).setOnClickListener(this);
        ViewPagerFixed viewPager = (ViewPagerFixed) findViewById(R.id.image_vp);
        bottomRl.setVisibility(View.GONE);
        actionBar.setOnClickListener(this);
         //
        ArrayList<ImageEntity> images = config.getImages();
        adapter = new PreviewAdapter(this, images);
        adapter.setPhotoViewClickListener(new PreviewClickList());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnPagerChange());
        //
        actionBar.setConfigsPreviewOnly(this, config, this);
        viewPager.setCurrentItem(config.previewIndex);
        onPageSelected(config.previewIndex);
    }

    @Override
    public void onClick(int id) {
        if (id == R.id.bra_back) {
            onBackPressed();
            return;
        }
    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setTitle((position + 1) + "/" + adapter.getCount());
    }
}
