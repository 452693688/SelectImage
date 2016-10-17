package com.app.config;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ConfigBuile {
    private static ConfigBuile build;

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


    public ConfigBuile setMore(boolean more) {
        isMore = more;
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

    public Configs build() {
        return new Configs(build);
    }

    private ConfigMoreBuile configMoreBuile;

    public ConfigMoreBuile getMoreBuile() {
        return configMoreBuile;
    }

    //设置选择更多
    public ConfigMoreBuile setMore() {
        if (configMoreBuile == null) {
            configMoreBuile = new ConfigMoreBuile();
        }
        isMore = true;
        return configMoreBuile;
    }

    //多选配置
    public class ConfigMoreBuile {
        //最多可多照片的数量
        private int imageSelectMaximum;


        public int getImageSelectMaximum() {
            return imageSelectMaximum;
        }

        public ConfigMoreBuile setImageSelectMaximum(int imageSelectMaximum) {
            this.imageSelectMaximum = imageSelectMaximum;
            return this;
        }

        public Configs builds() {
            return new Configs(build);
        }

    }

    private ConfigSingleBuile configSingleBuile;

    public ConfigSingleBuile getSingBuile() {
        return configSingleBuile;
    }

    //设置单选
    public ConfigSingleBuile setSingle() {
        if (configSingleBuile == null) {
            configSingleBuile = new ConfigSingleBuile();
        }
        isMore = false;
        return configSingleBuile;
    }

    //单选配置
    public class ConfigSingleBuile {
        private boolean isCrop;
        private int aspectX = 60;
        private int aspectY = 60;
        private int outputX = 60;
        private int outputY = 60;
        //只拍照
        private boolean onlyPhotograph;

        public boolean isOnlyPhotograph() {
            return onlyPhotograph;
        }

        public ConfigSingleBuile setOnlyPhotograph(boolean onlyPhotograph) {
            this.onlyPhotograph = onlyPhotograph;
            return this;
        }

        public boolean isCrop() {
            return isCrop;
        }

        public ConfigSingleBuile setCrop(boolean crop) {
            isCrop = crop;
            return this;
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

        //设置边框
        public ConfigSingleBuile setAspect(int aspectX, int aspectY) {
            setCrop(true);
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            return this;
        }

        //设置输出
        public ConfigSingleBuile setOutput(int outputX, int outputY) {
            setCrop(true);
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        public Configs build() {
            return new Configs(build);
        }
    }

    public boolean isMore() {
        return isMore;
    }

    public boolean isShowCamera() {
        return showCamera;
    }
}
