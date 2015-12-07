package com.baigu.ui.sample.ptr.refresh.ui.classic;


import com.baigu.ui.ptr.PtrClassicFrameLayout;
import com.baigu.ui.sample.ptr.R;

public class PullToRefresh extends WithTextViewInFrameLayoutFragment {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setHeaderTitle(R.string.ptr_demo_block_pull_to_refresh);
        ptrFrame.setPullToRefresh(true);
    }
}
