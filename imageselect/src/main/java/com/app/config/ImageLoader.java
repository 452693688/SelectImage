package com.app.config;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/20.
 */
public interface ImageLoader {
    void imageLoading(Context context, String path, ImageView imageView);
}
