package com.app.config.entity;

import java.io.Serializable;

public class ImageEntity implements Serializable {

    //图片路径
    public String imagePath;
    //图片名称
    public String imageName;
    //创建时间
    public long iamgeTime;
    //图片id
    public String imageId;
    //相册名字
    public String imageFileName;
    //图片类型
    public String iamgeType;
    //图片大小
    public String imageSize;
    //相册id
    public String imageFileId;
    // 经度
    public int iamgeAngle;
    //true 被选中
    public boolean isOption;
    public boolean originalShape;
}