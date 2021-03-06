package com.app.ui.bean;


import com.app.config.Configs;
import com.app.config.entity.ImageEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/6.
 */

public class PreviewImageBean implements Serializable {
    //照片文件索引
    public int imgFileIndex;
    //type 预览类型：1： 全局 2：只预览已选择的图片
    public int type;

    //选中的图片
    public ArrayList<ImageEntity> optionImage;
    public int index;
    public Configs config;
}
