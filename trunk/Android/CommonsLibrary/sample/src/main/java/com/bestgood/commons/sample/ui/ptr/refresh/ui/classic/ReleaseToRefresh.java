package com.bestgood.commons.sample.ui.ptr.refresh.ui.classic;


import com.bestgood.commons.sample.R;
import com.bestgood.commons.ui.ptr.PtrClassicFrameLayout;

public class ReleaseToRefresh extends WithTextViewInFrameLayoutFragment {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setHeaderTitle(R.string.ptr_demo_block_release_to_refresh);
        ptrFrame.setPullToRefresh(false);
    }
}
