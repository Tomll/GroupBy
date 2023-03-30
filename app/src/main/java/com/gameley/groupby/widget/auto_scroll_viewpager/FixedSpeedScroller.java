package com.gameley.groupby.widget.auto_scroll_viewpager;

import android.content.Context;
import android.widget.Scroller;

/**
 * 此类用于控制viewpager的切换速度
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 3000;//切换过程耗时

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int time) {
        mDuration = time;
    }

    public int getmDuration() {
        return mDuration;
    }
}
