package com.bestgood.commons.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bestgood.commons.R;
import com.bestgood.commons.network.http.AbsSpiceActivity;
import com.bestgood.commons.ui.pager.PageIndicator;
import com.bestgood.commons.ui.widget.TouchImageView;
import com.bestgood.commons.ui.widget.toast.ToastHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<String> list = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URL_LIST);
        int curItem = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PageIndicator indicator = (PageIndicator) findViewById(R.id.indicator);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new ImagePagerAdapter(list));
        indicator.setViewPager(viewPager);
        indicator.setCurrentItem(curItem);
    }

    private class ImagePagerAdapter extends PagerAdapter implements View.OnClickListener {

        private List<String> mList;

        public ImagePagerAdapter(List<String> list) {
            this.mList = list == null ? new ArrayList<String>() : new ArrayList<>(list);

            this.mList.addAll(Arrays.asList(
                    "http://h.hiphotos.baidu.com/image/pic/item/8435e5dde71190eff91e51ebcb1b9d16fdfa6019.jpg",
                    "http://h.hiphotos.baidu.com/image/pic/item/8435e5dde71190eff91e51ebcb1b9d16fdfa6019.jpg",
                    "http://h.hiphotos.baidu.com/image/pic/item/8435e5dde71190eff91e51ebcb1b9d16fdfa6019.jpg",
                    "http://a.hiphotos.baidu.com/image/pic/item/9e3df8dcd100baa10541858f4210b912c8fc2e91.jpg",
                    "http://d.hiphotos.baidu.com/image/pic/item/a08b87d6277f9e2f1c59df3c1a30e924b899f392.jpg",
                    "http://a.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b668543d7f81f4134970a5a64.jpg",
                    "http://f.hiphotos.baidu.com/image/pic/item/d439b6003af33a87844ede2ec35c10385343b5e7.jpg",
                    "http://b.hiphotos.baidu.com/image/pic/item/6a63f6246b600c33def400fc1f4c510fd9f9a171.jpg",
                    "http://a.hiphotos.baidu.com/image/pic/item/9e3df8dcd100baa10541858f4210b912c8fc2e91.jpg",
                    "http://d.hiphotos.baidu.com/image/pic/item/a08b87d6277f9e2f1c59df3c1a30e924b899f392.jpg",
                    "http://a.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b668543d7f81f4134970a5a64.jpg",
                    "http://f.hiphotos.baidu.com/image/pic/item/d439b6003af33a87844ede2ec35c10385343b5e7.jpg",
                    "http://b.hiphotos.baidu.com/image/pic/item/6a63f6246b600c33def400fc1f4c510fd9f9a171.jpg",
                    "http://h.hiphotos.baidu.com/image/pic/item/8435e5dde71190eff91e51ebcb1b9d16fdfa6019.jpg"
            ));
        }

        @Override
        public void onClick(View v) {
            finish();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imageLayout = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_comm_uil_touch_image_pager, container, false);
            final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.loading);

            final TouchImageView imageView = (TouchImageView) imageLayout.findViewById(R.id.image);
            //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imageView.setOnClickListener(this);
            imageLayout.setOnClickListener(this);

            final String url = mList.get(position);
            ImageLoader.getInstance().displayImage(url, imageView, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    //Logger.d("onLoadingStarted()");
                    displayStart();
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    //Logger.d("onLoadingFailed()");
                    displayFinish();
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    if (!TextUtils.isEmpty(message)) {
                        ToastHelper.show(getBaseContext(), message);
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                    //Logger.d("onLoadingComplete()");
                    displayFinish();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    //Logger.d("onLoadingCancelled()");
                    displayFinish();
                }

                private void displayStart() {
                    progressBar.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }

                private void displayFinish() {
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
            });

            container.addView(imageLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}