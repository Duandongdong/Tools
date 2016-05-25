package com.bestgood.commons.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bestgood.commons.R;
import com.bestgood.commons.activity.UILTouchImagePagerActivity;
import com.bestgood.commons.ui.pager.PageIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dengdingchun on 15/11/1.
 * 首页广告条自动滚动
 */
public class ImageSlidingView extends RelativeLayout {

    ViewPager mViewPager;
    PageIndicator mIndicator;

    private ImageOnClickListener mImageOnClickListener;

    public ImageSlidingView(Context context) {
        this(context, null);

    }

    public ImageSlidingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ImageSlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //======================================================================================
    private Handler handler = new Handler();
    private AutoCycleRunnable runnableCallbacks;


    private final class AutoCycleRunnable implements Runnable {
        public AutoCycleRunnable() {
        }

        @Override
        public void run() {
            if (null == mViewPager || null == mViewPager.getAdapter()) {
                return;
            }
            int childCount = mViewPager.getAdapter().getCount();
            if (childCount <= 0) {
                return;
            }
            int curItem = mViewPager.getCurrentItem();
            mIndicator.setCurrentItem((++curItem) % childCount);
        }
    }

    /**
     * 循环滚动顶部 热门推介 Fragment
     */
    public void startAutoCycle() {
        stopAutoCycle();
        runnableCallbacks = new AutoCycleRunnable();
        handler.postDelayed(runnableCallbacks, 3000);
    }

    /**
     * stop the auto circle
     */
    public void stopAutoCycle() {
        if (null != runnableCallbacks) {
            handler.removeCallbacks(runnableCallbacks);
            runnableCallbacks = null;
        }
    }

    //======================================================================================

    private ImagePagerAdapter mAdapter;

    public <T extends ImageSlidingItem> void initImageView(List<T> list, ImageOnClickListener listener) {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mIndicator = (PageIndicator) findViewById(R.id.indicator);

        this.mImageOnClickListener = listener;

        if (mAdapter == null) {
            mAdapter = new ImagePagerAdapter(list);
            mViewPager.setAdapter(mAdapter);
            mIndicator.setViewPager(mViewPager);
        } else {
            mAdapter.notifyDataSetChanged(list);
        }

        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                startAutoCycle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        startAutoCycle();
    }


    private class ImagePagerAdapter<T extends ImageSlidingItem> extends PagerAdapter {
        // Declare Variables
        private List<T> mList;
        private LayoutInflater mInflater;

        public ImagePagerAdapter(List<T> list) {
            this.mList = list == null ? new ArrayList<T>() : new ArrayList<T>(list);
            mInflater = LayoutInflater.from(getContext());
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = (ImageView) mInflater.inflate(R.layout.item_image_sliding_view, container, false);
            final ImageSlidingItem item = mList.get(position);
            if (item == null || TextUtils.isEmpty(item.getImageUrl())) {
                container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return imageView;
            }
            ImageLoader.getInstance().displayImage(item.getImageUrl(), imageView);

//        view.setBackgroundColor(Color.parseColor("#FF" + String.format("%06d", new Random().nextInt(999999))));
//        adIv.setBackgroundColor(Color.parseColor("#FF" + String.format("%06d", new Random().nextInt(999999))));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mImageOnClickListener != null) {
                        mImageOnClickListener.onImageItemClick(item);
                    } else {
                        UILTouchImagePagerActivity.goImagePagerActivity(getContext(), buildImageUrlList(), position);
                    }
                }
            });

            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void notifyDataSetChanged(List<T> list) {
            this.mList = list == null ? new ArrayList<T>() : new ArrayList<T>(list);
            super.notifyDataSetChanged();
        }

        public ArrayList<String> buildImageUrlList() {
            ArrayList<String> list = new ArrayList<String>();
            for (ImageSlidingItem item : mList) {
                if (item == null) {
                    continue;
                }
                list.add(item.getImageUrl());
            }
            return list;
        }
    }

    public interface ImageSlidingItem {
        String getImageUrl();
    }

    public interface ImageOnClickListener<T extends ImageSlidingItem> {
        void onImageItemClick(T item);
    }
}