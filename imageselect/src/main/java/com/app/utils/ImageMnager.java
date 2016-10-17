package com.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.app.bean.ImageBean;
import com.app.bean.ImageFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ImageMnager implements LoaderManager.LoaderCallbacks<Cursor> {
    private HashMap<String, List<ImageBean>> map;
    private boolean showCamera;
    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID};
    private Context contet;
    public static final int LOADER_ALL = 0;
    public static final int LOADER_CATEGORY = 1;
    public static final int REQUEST_CAMERA = 100;

    public ImageMnager(Context contet, boolean showCamera) {
        this.contet = contet;
        this.showCamera = showCamera;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(contet,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION,
                null, null, IMAGE_PROJECTION[2] + " DESC");
        return cursorLoader;
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        if (data == null && onLoadingListener != null) {
            onLoadingListener.onLoadingListener(null);
        }
        if (data == null) {
            return;
        }
        int count = data.getCount();
        map = new HashMap<>();
        ArrayList<ImageBean> imags = new ArrayList<>();
        if (count > 0) {
            data.moveToFirst();
            do {
                String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                File imageFile = new File(path);
                File folderFile = imageFile.getParentFile();
                String fileName = folderFile.getName();
                String filePath = folderFile.getAbsolutePath();
                //
                ImageBean image = new ImageBean(path,filePath, fileName, name, dateTime);
                List<ImageBean> list = map.get(filePath);
                if (list == null) {
                    list = new ArrayList<ImageBean>();
                }
                list.add(image);
                map.put(filePath, list);
                imags.add(image);
            } while (data.moveToNext());
            data.close();
        }
        if (onLoadingListener == null) {
            return;
        }
        Set<String> keys = map.keySet();
        List<ImageFile> fils = new ArrayList<>();
        fils.add(getFristBean(imags, 0));
        for (String key : keys) {
            List<ImageBean> list = map.get(key);
            fils.add(getFristBean(list, 1));
        }
        onLoadingListener.onLoadingListener(fils);
    }



    public ImageFile getFristBean(List<ImageBean> list, int type) {
        ImageFile file = new ImageFile();
        file.size = list.size();
        //需要拍照，添加一个默认照片
        if (showCamera) {
            ImageBean image = new ImageBean("", "","", "", 0);
            list.add(0, image);
        }
        file.imags = list;
        if (type == 0) {
            //全部
            file.setFileName("全部");
        }
        return file;
    }


    private OnLoadingListener onLoadingListener;

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
    }

    public interface OnLoadingListener {
        void onLoadingListener(List<ImageFile> fils);
    }
}
