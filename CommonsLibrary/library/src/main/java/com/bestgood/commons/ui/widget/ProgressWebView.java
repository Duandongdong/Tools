package com.bestgood.commons.ui.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bestgood.commons.R;

public class ProgressWebView extends WebView {

    private ProgressBar mProgressBar;

    @SuppressWarnings("deprecation")
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 4, 0, 0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);

        // setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
        // 是否可以缩放
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        // 配置权限（同样在WebChromeClient中实现）
        public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE) mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
