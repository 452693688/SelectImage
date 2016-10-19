package com.app.config;

import android.app.Activity;

/**
 * Created by Administrator on 2016/10/19.
 */
//多选配置
public class ConfigBuileMore {
    //最多可多照片的数量
    private int imageSelectMaximum;


    public int getImageSelectMaximum() {
        return imageSelectMaximum;
    }

    public ConfigBuileMore setImageSelectMaximum(int imageSelectMaximum) {
        this.imageSelectMaximum = imageSelectMaximum;
        return this;
    }

    public void build(Activity activity) {
        new Configs(ConfigBuile.build, activity);
    }

}
