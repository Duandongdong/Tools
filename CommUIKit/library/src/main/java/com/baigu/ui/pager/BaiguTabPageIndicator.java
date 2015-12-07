package com.baigu.ui.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class BaiguTabPageIndicator extends LinearLayout implements PageIndicator {

    private ViewPager mViewPager;
    private int mSelectedIndex;
    private TabClickListener mTabClickListener;

    public BaiguTabPageIndicator(Context context) {
        this(context, null);
    }

    public BaiguTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (view == null) {
            throw new IllegalStateException("ViewPager is null.");
        }
        if (mViewPager == view) {
            return;
        }

        PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final int index = i;
            View child = getChildAt(i);
            child.setClickable(true);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTabClickListener != null && !mTabClickListener.canTabChange(index)) {
                        return;
                    }
                    setCurrentItem(index);
                }
            });
        }
        if (mSelectedIndex > tabCount) {
            mSelectedIndex = tabCount - 1;
        }
        setCurrentItem(mSelectedIndex);
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedIndex = item;
        mViewPager.setCurrentItem(item, false);

        int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = getChildAt(i);
            child.setSelected(i == item);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(listener);
        }
    }

    public void setTabClickListener(TabClickListener tabClickListener) {
        this.mTabClickListener = tabClickListener;
    }

    public interface TabClickListener {
        boolean canTabChange(int index);
    }
}
