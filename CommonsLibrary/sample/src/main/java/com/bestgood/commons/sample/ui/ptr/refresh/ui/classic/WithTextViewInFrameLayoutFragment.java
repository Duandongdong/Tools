package com.bestgood.commons.sample.ui.ptr.refresh.ui.classic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.ui.ptr.PtrClassicFrameLayout;
import com.bestgood.commons.ui.ptr.PtrFrameLayout;
import com.bestgood.commons.ui.ptr.PtrHandler;

import in.srain.cube.mints.base.TitleBaseFragment;

public class WithTextViewInFrameLayoutFragment extends TitleBaseFragment {

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHeaderTitle(R.string.ptr_demo_block_frame_layout);

        final View contentView = inflater.inflate(R.layout.fragment_classic_header_with_viewgroup, container, false);

        final PtrClassicFrameLayout ptrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.fragment_rotate_header_with_view_group_frame);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
        });
        ptrFrame.setLastUpdateTimeRelateObject(this);

        // the following are default settings
        ptrFrame.setResistance(1.7f);
        ptrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrFrame.setDurationToClose(200);
        ptrFrame.setDurationToCloseHeader(1000);
        // default is false
        ptrFrame.setPullToRefresh(false);
        // default is true
        ptrFrame.setKeepHeaderWhenRefresh(true);

        // scroll then refresh
        // comment in base fragment
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                // ptrFrame.autoRefresh();
            }
        }, 150);

        setupViews(ptrFrame);

        return contentView;
    }

    protected void setupViews(final PtrClassicFrameLayout ptrFrame) {

    }
}