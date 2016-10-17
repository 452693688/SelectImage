package com.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bean.ImageBean;
import com.app.config.ConfigBuile;
import com.app.imageselect.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/17.
 */
public class ImagesAdapter extends BaseAdapter {
    private List<ImageBean> images = new ArrayList<ImageBean>();
    private AbsListView.LayoutParams itemLayoutParams;
    private ArrayList<String> paths = new ArrayList<>();
    private Context contex;

    public ImagesAdapter(Context contex) {
        this.contex = contex;
    }

    //设置照片的大小
    public void setItemSize(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;     // 屏幕宽度（像素）
        int height = dm.heightPixels;   // 屏幕高度（像素）
        float unW = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm);
        int w = (int) (width - unW) / 3;
        itemLayoutParams = new GridView.LayoutParams(w, w);
    }

    public void setData(List<ImageBean> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        if (i < 0 || i >= (images.size())) {
            return null;
        }
        return images.get(i);
    }

    public void addORremovePath(int index, int max) {
        String path = images.get(index).path;
        boolean isExist = paths.contains(path);
        if (isExist) {
            paths.remove(path);
        }
        if (!isExist && index == max) {
            Toast.makeText(contex, "已达选择上限", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isExist) {
            paths.add(path);
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HoldeView holdeView;
        if (view == null) {
            holdeView = new HoldeView();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_image, null);
            holdeView.imageIv = (ImageView) view.findViewById(R.id.item_image_iv);
            holdeView.imageTv = (TextView) view.findViewById(R.id.item_image_tv);

            holdeView.selectIv = (ImageView) view.findViewById(R.id.item_image_select_iv);
            view.setLayoutParams(itemLayoutParams);
            view.setTag(holdeView);
        } else {
            holdeView = (HoldeView) view.getTag();
        }
        ImageBean bean = images.get(i);
        if (i == 0) {
            holdeView.imageTv.setVisibility(View.VISIBLE);
            holdeView.imageIv.setVisibility(View.GONE);
            holdeView.imageIv.setImageResource(R.mipmap.imageselector_select_photo);
        } else {
            holdeView.imageTv.setVisibility(View.GONE);
            holdeView.imageIv.setVisibility(View.VISIBLE);
            ConfigBuile.getBuile().setImageLoading(viewGroup.getContext(),
                    bean.path, holdeView.imageIv);
        }
        int isSelect = paths.contains(bean.path) ? View.VISIBLE : View.GONE;
        holdeView.selectIv.setVisibility(isSelect);
        return view;
    }

    class HoldeView {
        public ImageView imageIv, selectIv;
        public TextView imageTv;
    }
}
