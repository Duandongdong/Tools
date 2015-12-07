package com.baigu.ui.sample.ptr.refresh.ui.classic;


import com.baigu.ui.ptr.PtrClassicFrameLayout;
import com.baigu.ui.sample.ptr.R;

public class KeepHeader extends WithTextViewInFrameLayoutFragment {

    @Override
    protected void setupViews(PtrClassicFrameLayout ptrFrame) {
        setHeaderTitle(R.string.ptr_demo_block_keep_header);
        ptrFrame.setKeepHeaderWhenRefresh(true);
    }
}
