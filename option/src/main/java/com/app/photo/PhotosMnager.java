package com.app.photo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

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
public class PhotosMnager {
    private HashMap<String, List<ImageEntity>> map;

    private static PhotosMnager manager;
    //全部照片
    private List<ImageFile> images;

    public static PhotosMnager getInstance() {
        if (manager == null) {
            manager = new PhotosMnager();
        }
        return manager;
    }

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

    public void setImgFile(List<ImageFile> images) {
        this.images = images;
    }

    public List<ImageFile> getImgFile() {
        return images;
    }

    public List<ImageEntity> getImgs(int index) {
        return images.get(index).imags;
    }

    /**
     * 通过uri 获取一张照片
     *
     * @param context
     * @param url
     * @return
     */
    public ImageEntity getImage(Context context, Uri url) {
        Cursor data = context.getContentResolver().query(url,
                IMAGE_PROJECTION, null, null, null);
        ImageEntity image = null;
        if (data != null) {
            data.moveToFirst();
            image = readCursor(data);
        }
        return image;
    }

    //高版本用
    public ImageEntity getImage2(Context context, Uri uri) {
        ImageEntity image = null;
        Cursor data = context.getContentResolver().query(uri, null,
                null, null, null);
        if (data != null) {
            data.moveToFirst();
            image = readCursor(data);
        }
        return image;
    }


    //创建一个“全部”文件夹
    public ImageFile getImageFile(List<ImageEntity> iamges,
                                  boolean showCamera, int type) {
        ImageFile file = new ImageFile();
        file.size = iamges.size();
        if (file.size != 0) {
            ImageEntity image = iamges.get(0);
            file.fileName = image.imageFileName;
            file.firstImagePath = image.imagePathSource;
            File imageFile = new File(image.imagePathSource);
            String filePath = imageFile.getParent();
            file.filePath = filePath;//new File(image.imagePathSource).getParentFile().getAbsolutePath();
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


    //==================设置监听======================================
    private OnLoadingListener onLoadingListener;

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
    }

    public interface OnLoadingListener {
        void onLoadingListener(List<ImageFile> fils);
    }

    //==================读取游标======================================
    private ImageEntity readCursor(Cursor data) {
        ImageEntity image = new ImageEntity();
        image.imagePathSource = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
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

    //================获取照片=====================================
    private Loader loader;
    private final int LOADER_ALL = 0;

    //请求获取照片
    public void doRequest(FragmentActivity activity, boolean isShowCamera) {
        if (loader == null) {
            loader = new Loader(activity, isShowCamera);
        }
        activity.getSupportLoaderManager().initLoader(LOADER_ALL, null, loader);
    }

    class Loader implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context context;
        private boolean showCamera;

        public Loader(Context context, boolean showCamera) {
            this.context = context;
            this.showCamera = showCamera;
        }

        @Override
        public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    null, null, IMAGE_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
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
            //全部照片
            fils.add(getImageFile(imags, showCamera, 0));
            //按文件夹分类照片
            for (String key : keys) {
                List<ImageEntity> list = map.get(key);
                fils.add(getImageFile(list, showCamera, 1));
            }
            onLoadingListener.onLoadingListener(fils);
        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

        }
    }
}
