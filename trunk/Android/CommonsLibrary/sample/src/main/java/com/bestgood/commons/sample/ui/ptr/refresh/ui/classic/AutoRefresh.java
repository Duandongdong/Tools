package com.bestgood.commons.sample.ui.ptr.refresh.ui.classic;


import com.bestgood.commons.sample.R;
import com.bestgood.commons.ui.ptr.PtrClassicFrameLayout;

public class AutoRefresh extends WithGridView {

    @Override
    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {
        ptrFrame.setLoadingMinTime(3000);
        setHeaderTitle(R.string.ptr_demo_block_auto_fresh);
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh(true);
            }
        }, 150);
    }
}