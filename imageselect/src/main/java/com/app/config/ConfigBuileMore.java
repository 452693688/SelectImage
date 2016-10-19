package com.app.config;

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

    public Configs builds() {
        return new Configs(ConfigBuile.build);
    }

}
