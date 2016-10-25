package com.app.bean;

public class ImageBean {
    public String parentPath;
    public String path;
    public String name;
    public long time;
    public String fileName;

    public ImageBean(String path, String parentPath, String fileName, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
        this.fileName = fileName;
        this.parentPath = parentPath;
    }


}