package com.bestgood.commons.network.http;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by dengdingchun on 15/10/24.
 */
public abstract class AbsSpiceFragment extends Fragment {

    private HttpRequestManager mHttpRequestManager = new HttpRequestManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHttpRequestManager.start(getActivity());
    }

    @Override
    public void onDestroy() {
        mHttpRequestManager.shouldStop();
        super.onDestroy();
    }

    public HttpRequestManager getRequestManager() {
        return mHttpRequestManager;
    }
}
