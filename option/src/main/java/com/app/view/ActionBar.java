package com.app.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.config.Configs;
import com.app.imageselect.R;

/**
 * Created by Administrator on 2016/10/19.
 */
public class ActionBar extends RelativeLayout {

    private TextView barTitleTv;
    public TextView barBackTv;
    public TextView barOptionTv;

    public ActionBar(Context context) {
        super(context);
        init(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);
        //
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        barBackTv = new TextView(context);
        barBackTv.setPadding(padding, 0, 0, 0);
        barBackTv.setGravity(Gravity.CENTER);
        barBackTv.setTextSize(15);
        barBackTv.setCompoundDrawablePadding(10);
        barBackTv.setId(R.id.bra_back);
        barBackTv.setLayoutParams(lp1);
        addView(barBackTv);
        //
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
        barTitleTv = new TextView(context);
        barTitleTv.setTextSize(18);
        barTitleTv.setId(R.id.bra_title);
        barTitleTv.setLayoutParams(lp2);
        addView(barTitleTv);
        //
        int optionHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, dm);
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, optionHeight);
        lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp3.addRule(RelativeLayout.CENTER_VERTICAL);

        lp3.rightMargin = padding;
        barOptionTv = new TextView(context);
        barOptionTv.setPadding(20, 5, 20, 5);
        barOptionTv.setGravity(Gravity.CENTER);
        barOptionTv.setTextSize(15);
        barOptionTv.setId(R.id.bra_option);
        barOptionTv.setLayoutParams(lp3);
        addView(barOptionTv);
    }

    public void setOptionText(String txt) {
        barOptionTv.setText(txt);
    }

    public void setConfigs(Activity activity, Configs config) {
        //设置默认hint
        if (!TextUtils.isEmpty(config.barBackHint)) {
            barBackTv.setText(config.barBackHint);
        }
        if (!TextUtils.isEmpty(config.barTitleHint)) {
            barTitleTv.setText(config.barTitleHint);
        }
        if (!TextUtils.isEmpty(config.barOptionHint)) {
            barOptionTv.setText(config.barOptionHint);
        }
        //设置action高度
        if (config.actionBarHeight > 80) {
            setMinimumHeight(config.actionBarHeight);
            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.height = config.actionBarHeight;
            setLayoutParams(lp);
        }
        if (config.barOptionHeight > 0) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) barOptionTv.getLayoutParams();
            lp.height = config.barOptionHeight;
            barOptionTv.setLayoutParams(lp);

        }
        //设置颜色
        if (config.statusBarColor != 0) {
            setWindowStatusBarColor(activity, config.statusBarColor);
        }
        if (config.actionBarColor != 0) {
            setBackgroundColor(config.actionBarColor);
        }
        if (config.barBackColor != 0) {
            barBackTv.setTextColor(config.barBackColor);
        }
        if (config.barTitleColor != 0) {
            barTitleTv.setTextColor(config.barTitleColor);
        }
        if (config.barOptionColor != 0) {
            barOptionTv.setTextColor(config.barOptionColor);
        }
        //设置字体大小
        if (config.barBackSize != 0) {
            barBackTv.setTextSize(config.barBackSize);
        }
        if (config.barTitleSize != 0) {
            barTitleTv.setTextSize(config.barTitleSize);
        }
        if (config.barOptionSize != 0) {
            barOptionTv.setTextSize(config.barOptionSize);
        }
        //设置图片
        if (config.barBackIconId != 0) {
            String hint = TextUtils.isEmpty(config.barBackHint) ? "" : config.barBackHint;
            setText(barBackTv, config.barBackIconId, hint, 0);
        }

        if (config.barOptionBackdropId != 0) {
            barOptionTv.setBackgroundResource(config.barOptionBackdropId);
        }
    }

    public void setWindowStatusBarColor(Activity activity, int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                ///底部导航栏//
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param tv
     * @param iconId
     * @param text
     * @param iconLocation 位置
     */
    public void setText(TextView tv, int iconId, String text, int iconLocation) {
        if (iconId != 0) {
            Drawable drawable = getContext().getResources().getDrawable(iconId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            switch (iconLocation) {
                case 0:
                    tv.setCompoundDrawables(drawable, null, null, null);
                    break;
                case 1:
                    tv.setCompoundDrawables(null, drawable, null, null);
                    break;
                case 2:
                    tv.setCompoundDrawables(null, null, drawable, null);
                    break;
                case 3:
                    tv.setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        }
        //tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setText(text);
    }
}
