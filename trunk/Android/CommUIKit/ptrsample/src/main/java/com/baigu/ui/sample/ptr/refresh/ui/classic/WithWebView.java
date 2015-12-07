package com.baigu.ui.sample.ptr.refresh.ui.classic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baigu.ui.ptr.PtrClassicFrameLayout;
import com.baigu.ui.ptr.PtrDefaultHandler;
import com.baigu.ui.ptr.PtrFrameLayout;
import com.baigu.ui.ptr.PtrHandler;
import com.baigu.ui.sample.ptr.R;

import in.srain.cube.mints.base.TitleBaseFragment;

public class WithWebView extends TitleBaseFragment {

    private PtrClassicFrameLayout mPtrFrame;
    private WebView mWebView;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHeaderTitle(R.string.ptr_demo_block_web_view);

        final View contentView = inflater.inflate(R.layout.fragment_classic_header_with_webview, null);
        mWebView = (WebView) contentView.findViewById(R.id.rotate_header_web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mPtrFrame.refreshComplete();
            }
        });
        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_web_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mWebView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
        return contentView;
    }

    private void updateData() {
        mWebView.loadUrl("http://www.liaohuqiu.net/");
    }
}