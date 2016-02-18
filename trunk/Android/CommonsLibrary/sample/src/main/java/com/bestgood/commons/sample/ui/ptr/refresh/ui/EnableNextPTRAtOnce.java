package com.bestgood.commons.sample.ui.ptr.refresh.ui;


import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.refresh.ui.classic.WithTextViewInFrameLayoutFragment;
import com.bestgood.commons.ui.ptr.PtrClassicFrameLayout;

public class EnableNextPTRAtOnce extends WithTextViewInFrameLayoutFragment {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setHeaderTitle(R.string.ptr_demo_enable_next_ptr_at_once);
        ptrFrame.setEnabledNextPtrAtOnce(true);
        ptrFrame.setPullToRefresh(false);
    }
}