package com.app.ui.bean;

import com.app.config.entity.ImageEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ImageFile {
    public List<ImageEntity> imags;
    public String filePath;
    //文件名
    public String fileName;
    //文件数量
    public int size;
    //显示的第一张图
    public String firstImagePath;

}
