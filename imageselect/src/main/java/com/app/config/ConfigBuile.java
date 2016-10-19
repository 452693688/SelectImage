package com.app.config;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ConfigBuile {
    public static ConfigBuile build;
    private String filePath = "/temp/pictures";
    //多选 or 单选
    private boolean isMore;
    //可开启相机
    private boolean showCamera;

    //
    public static ConfigBuile getNewBuile() {
        build = new ConfigBuile();
        return build;
    }

    public static ConfigBuile getBuile() {
        if (build == null) {
            build = new ConfigBuile();
        }
        return build;
    }

    public ConfigBuile setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
        return getBuile();
    }


    public ConfigBuile setBuileMore(boolean more) {
        isMore = more;
        return getBuile();
    }

    public String getFilePath() {
        return filePath;
    }

    public ConfigBuile setFilePath(String filePath) {
        this.filePath = filePath;
        return getBuile();
    }

    private ImageLoader imageLoader;

    public ConfigBuile setLoading(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return build;
    }

    public void setImageLoading(Context context, String path, ImageView imageView) {
        if (imageLoader == null) {
            return;
        }
        imageLoader.imageLoading(context, path, imageView);
    }

    public interface ImageLoader {
        void imageLoading(Context context, String path, ImageView imageView);
    }

    public boolean isMore() {
        return isMore;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public Configs build(Activity activity) {
        return new Configs(build,activity);
    }

    //设置选择更多
    private ConfigBuileMore configMoreBuile;

    public ConfigBuileMore setBuileMore() {
        if (configMoreBuile == null) {
            configMoreBuile = new ConfigBuileMore();
        }
        isMore = true;
        return configMoreBuile;
    }

    public ConfigBuileMore getBuileMore() {
        return configMoreBuile;
    }

    //设置单选
    private ConfigBuileSingle configSingleBuile;

    public ConfigBuileSingle setBuileSingle() {
        if (configSingleBuile == null) {
            configSingleBuile = new ConfigBuileSingle();
        }
        isMore = false;
        return configSingleBuile;
    }

    public ConfigBuileSingle getBuileSingle() {
        return configSingleBuile;
    }

    //设置actionbar
    private ConfigBuileBar configBuileBar;

    public ConfigBuileBar setBuileBar() {
        if (configBuileBar == null) {
            configBuileBar = new ConfigBuileBar();
        }
        return configBuileBar;
    }

    public ConfigBuileBar getBuileBar() {
        return configBuileBar;
    }


}
