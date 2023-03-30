package com.gameley.groupby.widget.auto_scroll_viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by dongrp on 2018/10/31.
 * 这个自定义布局：用作viewpager下方的页面指示点。当然也可以用到别的需要指示点的布局中去，该布局可以自定义指示点样式。
 */

public class TipPointGroup extends LinearLayout {

    private ArrayList<ImageView> pointList = new ArrayList<>();
    private Context context;
    int pointImageDrawableID;
    int width;
    int height;

    public TipPointGroup(Context context) {
        super(context);
        this.context = context;
    }

    public TipPointGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    /**
     * 设置point样式和 宽高
     *
     * @param pointImageDrawableID：指示点的backgroundDrawableID，用户需要根据自己项目实际需求定义drawable selector
     * @param width：pointImage的宽度
     * @param height：pointImage的高度
     */
    public void setIconAndSize(int pointImageDrawableID, int width, int height) {
        this.pointImageDrawableID = pointImageDrawableID;
        this.width = width;
        this.height = height;
    }

    /**
     * 只设置point样式,宽高由样式的内容决定
     *
     * @param pointImageDrawableID
     */
    public void setIconAndSize(int pointImageDrawableID) {
        this.pointImageDrawableID = pointImageDrawableID;
    }

    /**
     * 设置指示点个数
     *
     * @param count：指示点个数
     */
    public void upDataPointCount(int count) {
        pointList.clear();//清空pointList
        removeAllViews();//清空所有point view
        for (int i = 0; i < count; i++) {
            ImageView pointImage = new ImageView(context);
            pointList.add(pointImage);//添加pointImage
            addView(pointImage);
        }
        //默认选中第0个
        if (count > 0) {
            setSelectedPoint(0);
        }
    }

    /**
     * 设置选中的指示点
     *
     * @param position：选中的指示点的下标
     */
    public void setSelectedPoint(int position) {
        for (int i = 0; i < pointList.size(); i++) {
            ImageView pointImage = pointList.get(i);
            //设置选中状态
            if (i == position) {
                pointImage.setSelected(true);
            } else {
                pointImage.setSelected(false);
            }
            //设置宽高
            if (width != 0 && height != 0) {
                LayoutParams layoutParams = new LayoutParams(width, height);
                layoutParams.setMarginEnd(10);
                pointImage.setLayoutParams(layoutParams);
            } else {
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMarginEnd(10);
                pointImage.setLayoutParams(layoutParams);
            }
            //设置图内容
            pointImage.setImageDrawable(context.getDrawable(pointImageDrawableID));
        }
    }


}
