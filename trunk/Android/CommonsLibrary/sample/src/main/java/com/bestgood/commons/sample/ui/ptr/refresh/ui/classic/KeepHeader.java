package com.bestgood.commons.sample.ui.ptr.refresh.ui.classic;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.ui.ptr.PtrClassicFrameLayout;

public class KeepHeader extends WithTextViewInFrameLayoutFragment {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setHeaderTitle(R.string.ptr_demo_block_keep_header);
        ptrFrame.setKeepHeaderWhenRefresh(true);
    }
}
