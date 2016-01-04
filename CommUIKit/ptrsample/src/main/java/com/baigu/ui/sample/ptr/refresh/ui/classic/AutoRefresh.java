package com.baigu.ui.sample.ptr.refresh.ui.classic;

import com.baigu.ui.ptr.PtrClassicFrameLayout;
import com.baigu.ui.sample.ptr.R;

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