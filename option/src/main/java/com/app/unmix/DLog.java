package com.app.unmix;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/28.
 */

public class DLog {
    private static boolean isDebug;

    public static void e(String tag, Object obj) {
        if (!isDebug) {
            return;
        }
        Log.e(tag, String.valueOf(obj));
    }

    public static void e(Object obj) {
        if (!isDebug) {
            return;
        }
        Log.e("===", String.valueOf(obj));
    }

    public static void setIsDebug(boolean debug) {
        isDebug = debug;
    }
}
