package com.app.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

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
    public static File createTmpFile(Context context, String filePath) {

        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());

        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStorageDirectory() + filePath);

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, timeStamp + ".png");
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, timeStamp + ".png");
        }

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

    public static void hideTitleBar(Activity activity, int resource, int steepToolBarColor) {
        if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            LinearLayout whole_layout = (LinearLayout) activity.findViewById(resource);
            whole_layout.setBackgroundColor(steepToolBarColor);
            whole_layout.setPadding(0, activity.getResources().getDimensionPixelSize(getStatusBarHeight()), 0, 0);
        }
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
