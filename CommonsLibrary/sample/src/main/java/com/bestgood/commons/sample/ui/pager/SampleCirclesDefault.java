package com.bestgood.commons.sample.ui.pager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.ui.pager.CirclePageIndicator;

public class SampleCirclesDefault extends BaseSampleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_circles);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}