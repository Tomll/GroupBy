package com.gameley.groupby.widget.auto_scroll_viewpager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by dongrp on 2016/10/11.
 */

public class AutoViewPagerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private OnAutoViewPagerItemClickListener listener;
    private Context mContext;
    private AutoViewPager mView;
    private List<T> data;

    //构造
    public AutoViewPagerAdapter(Context context, List<T> data, OnAutoViewPagerItemClickListener listener) {
        this.mContext = context;
        this.listener = listener;
        this.data = data;
    }

    //传入AutoViewPager进行初始化
    public void init(AutoViewPager autoViewPager, FixedSpeedScroller fixedSpeedScroller) {
        mView = autoViewPager;
        mView.addOnPageChangeListener(this);
        mView.setAdapter(this);
        //mView.setPageTransformer(true, new ZoomOutPageTransformer()); //设置切换效果
        //通过反射:传入fixedSpeedScroller，控制viewpager的切换速度
        if (null != fixedSpeedScroller) {
            try {
                Field mField = ViewPager.class.getDeclaredField("mScroller");
                mField.setAccessible(true);
                mField.set(mView, fixedSpeedScroller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //设置初始值为中间，这样一开始就能够往左滑动了
        if (getRealCount() > 1) {
            int position = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
            mView.setCurrentItem(position);
        }
        //开始播放
        mView.start();
    }

    public void notifyDataSetChanged(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
        mView.upDataPointCount(getRealCount());
    }


    public int getRealCount() {
        return data == null ? 0 : data.size();
    }

    //以下方法是：本类继承PagerAdapter后重写父类中的方法
    @Override
    public int getCount() {
        if (null == data) {
            return 0;
        } else if (data.size() <= 1) {
            return data.size();
        } else {
            return Integer.MAX_VALUE;
        }
//        return data == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && getRealCount() != 0) {
                    listener.onItemClick(position % getRealCount(), data.get(position % getRealCount()));
                }
            }
        });
        Glide.with(mContext).load(data.get(position % getRealCount())).into(imageView);
//        if (getRealCount() != 0) {
//            if (data.get(position % getRealCount()).equals("D9")) {//D9 预置banner
//                switch (position % getRealCount()) {
//                    case 0:
//                        GlideUtil.loadInto(R.mipmap.d9banner_00, imageView, mContext);
//                        break;
//                    case 1:
//                        GlideUtil.loadInto(R.mipmap.d9banner_01, imageView, mContext);
//                        break;
//                    case 2:
//                        GlideUtil.loadInto(R.mipmap.d9banner_02, imageView, mContext);
//                        break;
//                }
//            } else if (data.get(position % getRealCount()).equals("S5D")) {//S5 D渠道预置banner
//                switch (position % getRealCount()) {
//                    case 0:
//                        GlideUtil.loadInto(R.mipmap.s5banner_01, imageView, mContext);
//                        break;
//                }
//            } else if (data.get(position % getRealCount()).equals("S5S")) {//S5 S渠道预置banner
//                switch (position % getRealCount()) {
//                    case 0:
//                        GlideUtil.loadInto(R.mipmap.s5banner_00, imageView, mContext);
//                        break;
//                }
//            } else if (data.get(position % getRealCount()).equals("S5E")) {//S5 E渠道预置banner
//                switch (position % getRealCount()) {
//                    case 0:
//                        GlideUtil.loadInto(R.mipmap.e_banner, imageView, mContext);
//                        break;
//                }
//            } else if (data.get(position % getRealCount()).equals("E2")) {//E2 预置banner
//                switch (position % getRealCount()) {
//                    case 0:
//                        String bannerPath0 = ProductConfig.PATH_THEME + "/banner/banner_0.png";
//                        GlideUtil.loadInto(bannerPath0, imageView, mContext);
//                        break;
//                }
//            } else if (data.get(position % getRealCount()).equals("A6")) {//A6 预置banner
//                switch (position % getRealCount()) {
//                    case 0:
//                        GlideUtil.loadInto(R.mipmap.e1banner01, imageView, mContext);
//                        break;
//                    case 1:
//                        GlideUtil.loadInto(R.mipmap.e1banner03, imageView, mContext);
//                        break;
//                }
//            } else {
//                GlideUtil.loadInto(data.get(position % getRealCount()), imageView, mContext);
//            }
//        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }


    //以下方法是：ViewPager.OnPageChangeListener 接口回调
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (getRealCount() != 0) {
            mView.onPageSelected(position % getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //自定义的AutoViewPager中的item点击监听接口
    public interface OnAutoViewPagerItemClickListener<T> {
        void onItemClick(int position, T t);
    }

    //此类用于控制ViewPager的切换效果
/*    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1) 不可见状态
                // This page is way off-screen to the left.
                view.setAlpha(0); //透明度设置为0

            } else if (position <= 1) { // [-1,1] 可见状态，设置动画效果
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity] 不可见状态
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }*/


}