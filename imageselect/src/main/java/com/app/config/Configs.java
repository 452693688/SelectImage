package com.app.config;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Configs implements Serializable {
    public static final int TASK_COMPLETE = 200;
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
    public String barTitleHint;
    public String barOptionHint;
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
    public int aspectX;
    public int aspectY;
    public int outputX;
    public int outputY;

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

    public Configs(ConfigBuile build) {
        showCamera = build.isShowCamera();
        isMore = build.isMore();
        filePath = build.getFilePath();
        ConfigBuileMore moreBuile = build.getBuileMore();
        ConfigBuileSingle singBuile = build.getBuileSingle();
        ConfigBuileBar buileBar = build.getBuileBar();
        setBuileMore(moreBuile);
        setBuileSingle(singBuile);
        setBuileBar(buileBar);
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
        barOptionHint = buileBar.getBarOptionHint();
    }

}
