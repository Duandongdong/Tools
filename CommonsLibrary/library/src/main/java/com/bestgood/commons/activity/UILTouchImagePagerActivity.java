package com.bestgood.commons.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bestgood.commons.R;
import com.bestgood.commons.network.http.AbsSpiceActivity;
import com.bestgood.commons.ui.pager.PageIndicator;
import com.bestgood.commons.ui.widget.TouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class UILTouchImagePagerActivity extends AbsSpiceActivity {

    private static final String EXTRA_IMAGE_URL_LIST = "com.bestgood.commons.EXTRA_IMAGE_URL_LIST";
    private static final String EXTRA_CURRENT_POSITION = "com.bestgood.commons.EXTRA_CURRENT_POSITION";

    public static void goImagePagerActivity(Context context, ArrayList<String> imageUrlList, int curPosition) {
        Intent intent = new Intent(context, UILTouchImagePagerActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL_LIST, imageUrlList);
        intent.putExtra(EXTRA_CURRENT_POSITION, curPosition);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_uil_touch_image_pager);

        initView();
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PageIndicator indicator = (PageIndicator) findViewById(R.id.indicator);


        List<String> list = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URL_LIST);
        int curItem = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);


        ImagePagerAdapter adapter = new ImagePagerAdapter(list);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);


        if (curItem > 0 && curItem < adapter.getCount()) {
            indicator.setCurrentItem(curItem);
        }

    }

    private class ImagePagerAdapter extends PagerAdapter {

        private List<String> mList;

        public ImagePagerAdapter(List<String> list) {
            this.mList = list == null ? new ArrayList<String>() : new ArrayList<>(list);
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
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView imageView = new TouchImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            final String url = mList.get(position);
            if (TextUtils.isEmpty(url)) {
                container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return imageView;
            }

            ImageLoader.getInstance().displayImage(url, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
