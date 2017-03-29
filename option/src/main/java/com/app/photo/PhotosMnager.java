package com.app.photo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.app.config.entity.ImageEntity;
import com.app.ui.bean.ImageFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/17.
 */
public class PhotosMnager implements LoaderManager.LoaderCallbacks<Cursor> {
    private HashMap<String, List<ImageEntity>> map;
    private boolean showCamera;

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,                  //图片路径
            MediaStore.Images.Media.DISPLAY_NAME,          //图片名称
            MediaStore.Images.Media.DATE_ADDED,            //创建时间
            MediaStore.Images.Media._ID,                   //图片id
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,   //相册名字
            MediaStore.Images.Media.MIME_TYPE,             //图片类型
            MediaStore.Images.Media.SIZE,                  //图片大小
            MediaStore.Images.Media.BUCKET_ID,             //相册id
            MediaStore.Images.Media.LONGITUDE,             // 经度
    };

    private Context contet;
    public static final int LOADER_ALL = 0;

    public PhotosMnager(Context contet, boolean showCamera) {
        this.contet = contet;
        this.showCamera = showCamera;
    }

    public ImageEntity getImage(Uri url) {
        Cursor data = contet.getContentResolver().query(url,
                IMAGE_PROJECTION, null, null, null);
        ImageEntity image = null;
        if (data != null) {
            data.moveToFirst();
            image = readCursor(data);
        }
        return image;
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
        ArrayList<ImageEntity> imags = new ArrayList<>();
        if (count > 0) {
            data.moveToFirst();
            do {
                ImageEntity image = readCursor(data);
                List<ImageEntity> list = map.get(image.imageFileId);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(image);
                map.put(image.imageFileId, list);
                imags.add(image);
            } while (data.moveToNext());
            data.close();
        }
        if (onLoadingListener == null) {
            return;
        }
        Set<String> keys = map.keySet();
        //文件夹
        List<ImageFile> fils = new ArrayList<>();
        fils.add(getImageFile(imags, 0));
        for (String key : keys) {
            List<ImageEntity> list = map.get(key);
            fils.add(getImageFile(list, 1));
        }
        onLoadingListener.onLoadingListener(fils);
    }

    private ImageEntity readCursor(Cursor data) {
        ImageEntity image = new ImageEntity();
        image.imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
        image.imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
        image.iamgeTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
        image.imageId = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
        image.imageFileName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
        image.iamgeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
        image.imageSize = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
        image.imageFileId = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7]));
        image.iamgeAngle = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[8]));
        return image;
    }

    //给图片创建一个文件夹
    public ImageFile getImageFile(List<ImageEntity> iamges, int type) {
        ImageFile file = new ImageFile();
        file.size = iamges.size();
        if (file.size != 0) {
            ImageEntity image = iamges.get(0);
            file.fileName = image.imageFileName;
            file.firstImagePath = image.imagePath;
            File imageFile = new File(image.imagePath);
            String filePath=imageFile.getParent();
            file.filePath = filePath;//new File(image.imagePath).getParentFile().getAbsolutePath();
        }
        if (showCamera) {
            //需要拍照，添加一个默认照片
            ImageEntity image = new ImageEntity();
            iamges.add(0, image);
        }
        file.imags = iamges;
        if (type == 0) {
            //全部
            file.fileName = "全部";
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
