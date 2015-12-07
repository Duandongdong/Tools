package com.baigu.ui.sample.ptr.loadmore.ui.loadmore;


import com.baigu.ui.ptr.PtrFrameLayout;
import com.baigu.ui.ptr.PtrUIHandler;
import com.baigu.ui.ptr.indicator.PtrIndicator;

public abstract class PtrUIRefreshCompleteHandler implements PtrUIHandler {

    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
