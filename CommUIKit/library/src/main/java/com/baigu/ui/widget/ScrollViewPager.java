package com.baigu.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author ddc
 * @date: Aug 11, 2014 11:32:26 PM
 */
public class ScrollViewPager extends ViewPager {
    private boolean isCanScroll = false;

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return isCanScroll ? super.canScroll(v, checkV, dx, x, y) : true;
    }
}
