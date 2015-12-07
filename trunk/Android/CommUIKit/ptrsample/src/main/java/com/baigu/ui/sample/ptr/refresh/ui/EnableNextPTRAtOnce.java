package com.baigu.ui.sample.ptr.refresh.ui;


import com.baigu.ui.ptr.PtrClassicFrameLayout;
import com.baigu.ui.sample.ptr.R;
import com.baigu.ui.sample.ptr.refresh.ui.classic.WithTextViewInFrameLayoutFragment;

public class EnableNextPTRAtOnce extends WithTextViewInFrameLayoutFragment {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setHeaderTitle(R.string.ptr_demo_enable_next_ptr_at_once);
        ptrFrame.setEnabledNextPtrAtOnce(true);
        ptrFrame.setPullToRefresh(false);
    }
}