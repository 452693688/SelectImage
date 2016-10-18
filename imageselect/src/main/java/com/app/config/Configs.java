package com.app.config;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Configs implements Serializable {
    public static final int TASK_COMPLETE = 200;
    //可开启相机
    private boolean showCamera;
    //多选
    private boolean isMore;
    //最多可多照片的数量
    private int imageSelectMaximum;
    //照片存储路径
    private String filePath;
    //裁剪
    private boolean isCrop;
    private int aspectX;
    private int aspectY;
    private int outputX;
    private int outputY;

    //只拍照
    private boolean onlyPhotograph;

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
        ConfigBuile.ConfigMoreBuile moreBuile = build.getMoreBuile();
        ConfigBuile.ConfigSingleBuile singBuile = build.getSingBuile();
        setMore(moreBuile);
        setSingle(singBuile);
    }

    private void setMore(ConfigBuile.ConfigMoreBuile moreBuile) {
        if (moreBuile == null) {
            return;
        }
        imageSelectMaximum = moreBuile.getImageSelectMaximum();
    }

    private void setSingle(ConfigBuile.ConfigSingleBuile singBuile) {
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

    public int getImageSelectMaximum() {
        return imageSelectMaximum;
    }

    public void setImageSelectMaximum(int imageSelectMaximum) {
        this.imageSelectMaximum = imageSelectMaximum;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public void setCrop(boolean crop) {
        isCrop = crop;
    }

    public int getAspectX() {
        return aspectX;
    }

    public void setAspectX(int aspectX) {
        this.aspectX = aspectX;
    }

    public int getAspectY() {
        return aspectY;
    }

    public void setAspectY(int aspectY) {
        this.aspectY = aspectY;
    }

    public int getOutputX() {
        return outputX;
    }

    public void setOutputX(int outputX) {
        this.outputX = outputX;
    }

    public int getOutputY() {
        return outputY;
    }

    public void setOutputY(int outputY) {
        this.outputY = outputY;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isOnlyPhotograph() {
        return onlyPhotograph;
    }

    public void setOnlyPhotograph(boolean onlyPhotograph) {
        this.onlyPhotograph = onlyPhotograph;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
