package com.baigu.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dengdingchun on 15/10/24.
 * <p/>
 * 自定义ViewPager,可以设置ViewPager是否可以滑动切换Page页面
 */
public class SlidingViewPager extends ViewPager {
    //    private boolean isCanScroll = true;
    private boolean isPagingEnabled = false;

    public SlidingViewPager(Context context) {
        super(context);
    }

    public SlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }


//    @Override
//    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
////        return isCanScroll ? super.canScroll(v, checkV, dx, x, y) : false;
//        return true;
//    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

//    public void setCanScroll(boolean isCanScroll) {
//        this.isCanScroll = isCanScroll;
//    }
}
