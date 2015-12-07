package com.baigu.ui.sample.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baigu.ui.sample.R;

/**
 * Created by dengdingchun on 15/11/23.
 * <p/>
 * WebView
 */
public class WebViewActivity extends Activity {
    // private View mLoadingView;
    protected WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_webview);

        mWebView = (WebView) findViewById(R.id.webview);

//        initData();
        initData2();
    }


    private void initData() {
        //array of div's ids to disable/hide
        final String[] idsToHide = {"section1", "section3", "section5"};

        //enable javascript engine
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //run 'disableSection' for all divs to hide/disable
//                for (String s : idsToHide) {
//                    String surveyId = s;
//                    view.loadUrl("javascript:disableSection('" + surveyId + "');");
//                }
                view.loadUrl("javascript:document.getElementById('section1').style.display = 'none'; void(0);");
            }
        });
        //load webpage from assets
        mWebView.loadUrl("file:///android_asset/list.html");
    }

    private void initData2() {
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        // 最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 清除浏览器缓存
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);


        mWebView.setWebViewClient(new WebViewClient() {
            /**
             * shouldOverrideUrlLoading:在web页面里单击链接的时候，会自动调用android自带的浏览器来打开链接，
             * 需要通过该方法在本页面打开；
             */
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                // view.loadUrl(url);
                return false;
            }

            /**
             * 加载资源时响应
             */
            public void onLoadResource(android.webkit.WebView view, String url) {
                view.loadUrl("javascript:document.getElementsByClassName('ss_title')[0].style.display = 'none';");
            }

            /**
             * 在加载页面时响应
             */
            public void onPageStarted(android.webkit.WebView view, String url, android.graphics.Bitmap favicon) {
                WebSettings set = view.getSettings();
                set.setDomStorageEnabled(true);
                set.setBlockNetworkImage(false);
                set.setBlockNetworkLoads(false);
            }

            /**
             * 在加载页面结束时响应
             */
            public void onPageFinished(android.webkit.WebView view, String url) {
                //view.loadUrl("javascript:document.getElementById(ss_title).style.display = 'none'");
//                view.loadUrl("javascript:(function() { "
//                        + "document.getElementById(ss_title).style.display=\"none\"; "
//                        + "})()");
                //view.loadUrl("javascript:document.getElementById('ss_title').style.display = 'none'; void(0);");
                view.loadUrl("javascript:document.getElementsByClassName('ss_title')[0].style.display = 'none';");
                //view.loadUrl("javascript:document.getElementByClassName('ss_title').style.display = 'none'; void(0);");
            }


            /**
             * 在加载出错时响应
             */
            public void onReceivedError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
            }


            /**
             * request数据时调用
             */
            public void onReceivedHttpAuthRequest(android.webkit.WebView view, android.webkit.HttpAuthHandler handler, String host, String realm) {
            }
        });


        mWebView.loadUrl("http://service.car.118114.net/qcgh/getGuoHuView?type=jcsp");
    }
}
