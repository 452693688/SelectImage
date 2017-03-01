package com.app.config;

import android.app.Activity;

/**
 * Created by Administrator on 2016/10/19.
 */
//单选配置
public class ConfigBuileSingle {
    private boolean isOnlyPhotograph;
    private boolean isCrop;
    private boolean isSystemCrop;
    private int aspectX = 60;
    private int aspectY = 60;
    private int outputX = 60;
    private int outputY = 60;

    public boolean isCrop() {
        return isCrop;
    }

    public ConfigBuileSingle setCrop(boolean crop) {
        isCrop = crop;
        return this;
    }
    public boolean isSystemCrop() {
        return isSystemCrop;
    }

    public ConfigBuileSingle setSystemCrop(boolean systemCrop) {
        isSystemCrop = systemCrop;
        return this;
    }
    //设置边框
    public ConfigBuileSingle setAspect(int aspectX, int aspectY) {
        setCrop(true);
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        return this;
    }

    public ConfigBuileSingle setOutput(int outputX, int outputY) {
        setCrop(true);
        this.outputX = outputX;
        this.outputY = outputY;
        return this;
    }

    public ConfigBuileSingle setOnlyPhotograph(boolean onlyPhotograph) {
        isOnlyPhotograph = onlyPhotograph;
        return this;
    }

    public void build(Activity activity) {
        new Configs(ConfigBuile.build, activity);
    }
    //

    public boolean isOnlyPhotograph() {
        return isOnlyPhotograph;
    }

    public int getAspectX() {
        return aspectX;
    }

    public int getAspectY() {
        return aspectY;
    }

    public int getOutputX() {
        return outputX;
    }

    public int getOutputY() {
        return outputY;
    }
}

