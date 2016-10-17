package com.app.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ImageFile {
    public List<ImageBean> imags;
    //文件名
    private String name;
    //文件数量
    public int size;
    //显示的第一张图
    private String fileImage;

    //获取一张图片
    public String getFileImage() {
        if (TextUtils.isEmpty(fileImage)) {
            fileImage = imags.get(1).path;
        }
        return fileImage;
    }

    //获取文件名称
    public String getFileName() {
        if (TextUtils.isEmpty(name)) {
            name = imags.get(1).fileName;
        }
        return name;
    }

    public void setFileName(String name) {
        this.name = name;
    }

    //获取文件图片数量
    public int getSize() {
        return size;
    }

    public List<ImageBean> getFiles() {
        return imags;
    }

    public void setFiles(List<ImageBean> files) {
        this.imags = files;
    }
}
