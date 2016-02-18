package com.bestgood.commons.sample.ui.ptr.refresh.ui.classic;

import android.view.View;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.refresh.ui.Utils;
import com.bestgood.commons.ui.ptr.PtrClassicFrameLayout;
import com.bestgood.commons.ui.ptr.PtrDefaultHandler;
import com.bestgood.commons.ui.ptr.PtrFrameLayout;


public class HideHeader extends WithTextViewInFrameLayoutFragment {

    @Override
    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {
        setHeaderTitle(R.string.ptr_demo_block_hide_header);
        ptrFrame.setKeepHeaderWhenRefresh(false);

        final View loading = Utils.createSimpleLoadingTip(getContext());
        mTitleHeaderBar.getRightViewContainer().addView(loading);

        ptrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loading.setVisibility(View.VISIBLE);
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.INVISIBLE);
                        ptrFrame.refreshComplete();
                    }
                }, 1500);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
        });
    }
}