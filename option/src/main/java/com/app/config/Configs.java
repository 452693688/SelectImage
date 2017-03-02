package com.app.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.activity.IncidentActivity;
import com.app.photo.DataStore;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Configs implements Serializable {
    public static final int TASK_START = 200;
    public static final int TASK_PICTURE_COMPLETE = 201;
    public static final int TASK_CROP_COMPLETE = 202;
    public static final int TASK_CANCEL = 203;
    public static final String TASK_COMPLETE_RESULT = "result";
    //导航条
    public int statusBarColor;
    public int actionBarColor;
    public int barBackColor;
    public int barTitleColor;
    public int barOptionColor;
    // px
    public int actionBarHeight;
    public int barOptionHeight;
    //
    public int barBackSize;
    public int barTitleSize;
    public int barOptionSize;
    //
    public int barBackIconId;
    public int barOptionBackdropId;
    //
    public String barBackHint;
    public String barTitleHint, barCorpTitleHint;
    public String barOptionHint, barCorpOptionHint;
    //可开启相机
    public boolean showCamera;
    //多选
    public boolean isMore;
    //最多可多照片的数量
    public int imageSelectMaximum;
    //照片存储路径
    public String filePath;
    //裁剪
    public boolean isCrop;
    //是否调用系统裁剪
    public boolean isNotSystemCrop;
    public int aspectX;
    public int aspectY;
    public int outputX;
    public int outputY;
    public boolean isDebug;
    //只拍照
    public boolean onlyPhotograph;

    public Configs(int imageSelectMaximum, boolean isMore, boolean isCrop, int aspectX, int aspectY, int outputX, int outputY, boolean showCamera) {
        this.imageSelectMaximum = imageSelectMaximum;
        this.isMore = isMore;
        this.isCrop = isCrop;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.outputX = outputX;
        this.outputY = outputY;
        this.showCamera = showCamera;
    }

    public Configs(ConfigBuile build, Activity activity) {
        showCamera = build.isShowCamera();
        isMore = build.isMore();
        filePath = build.getFilePath();
        isDebug = build.isDebug();
        ConfigBuileMore moreBuile = build.getBuileMore();
        ConfigBuileSingle singBuile = build.getBuileSingle();
        ConfigBuileBar buileBar = build.getBuileBar();
        setBuileMore(moreBuile);
        setBuileSingle(singBuile);
        setBuileBar(buileBar);
        //
        DataStore.restData(activity);
        Intent it = new Intent(activity, IncidentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", this);
        it.putExtras(bundle);
        activity.startActivityForResult(it, TASK_START);
    }

    private void setBuileMore(ConfigBuileMore moreBuile) {
        if (moreBuile == null) {
            return;
        }
        imageSelectMaximum = moreBuile.getImageSelectMaximum();
    }

    private void setBuileSingle(ConfigBuileSingle singBuile) {
        if (singBuile == null) {
            return;
        }
        isCrop = singBuile.isCrop();
        isNotSystemCrop = singBuile.isNotSystemCrop();
        aspectX = singBuile.getAspectX();
        aspectY = singBuile.getAspectY();
        outputX = singBuile.getOutputX();
        outputY = singBuile.getOutputY();
        onlyPhotograph = singBuile.isOnlyPhotograph();
    }

    private void setBuileBar(ConfigBuileBar buileBar) {
        if (buileBar == null) {
            return;
        }
        statusBarColor = buileBar.getStatusBarColor();
        actionBarColor = buileBar.getActionBarColor();
        barBackColor = buileBar.getBarBackColor();
        barTitleColor = buileBar.getBarTitleColor();
        barOptionColor = buileBar.getBarOptionColor();
        //
        actionBarHeight = buileBar.getActionBarHeight();
        barOptionHeight = buileBar.getBarOptionHeight();
        //
        barBackSize = buileBar.getBarBackSize();
        barTitleSize = buileBar.getBarTitleSize();
        barOptionSize = buileBar.getBarOptionSize();
        //
        barBackIconId = buileBar.getBarBackIconId();
        barOptionBackdropId = buileBar.getBarOptionBackdropId();
        //
        barBackHint = buileBar.getBarBackHint();
        barTitleHint = buileBar.getBarTitleHint();
        barCorpTitleHint = buileBar.getBarCorpTitleHint();
        barOptionHint = buileBar.getBarOptionHint();
        barCorpOptionHint = buileBar.getBarCorpOptionHint();
        //
        if (isCrop && TextUtils.isEmpty(barCorpTitleHint)) {
            barCorpTitleHint = buileBar.getBarTitleHint();
        }
        if (isCrop && TextUtils.isEmpty(barCorpOptionHint)) {
            barCorpOptionHint = buileBar.getBarOptionHint();
        }
        if (!isMore) {
            barOptionHint = "";
        }

    }

}
