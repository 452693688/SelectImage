package com.app.photo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guom on 2016/10/17.
 */
public class FileUtile {
    private final static String PATTERN = "yyyyMMddHHmmss";

    //拍照获取 File
    public static File createPhotoFile(Context context, String filePath) {
        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());
        File dir = new File(Environment.getExternalStorageDirectory() + filePath);
        if (FileUtile.existSDCard()) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, timeStamp + ".png");
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, timeStamp + ".png");
        }

    }

    public static File createCropFile(Context context, String filePath) {
        File file ;
        if (FileUtile.existSDCard()) {
            file = new File(Environment.getExternalStorageDirectory() + filePath,
                    FileUtile.getImageName());
        } else {
            file = new File(context.getCacheDir(), FileUtile.getImageName());
        }
        return file;
    }

    //
    public static int getStatusBarHeight() {
        Class<?> c;
        Object obj;
        Field field;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return x;
    }

    /**
     * exist SDCard
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //裁剪获取 File
    public static String getImageName() {
        String PATTERN = "yyyyMMddHHmmss";
        return new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date()) + ".png";
    }

}
