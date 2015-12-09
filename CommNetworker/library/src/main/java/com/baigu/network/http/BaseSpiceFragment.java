package com.baigu.network.http;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by dengdingchun on 15/10/24.
 */
public class BaseSpiceFragment extends Fragment {

    private RequestManager mRequestManager = new RequestManager(HttpClientService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestManager.start(getActivity());
    }

    @Override
    public void onDestroy() {
        mRequestManager.shouldStop();
        super.onDestroy();
    }

    public RequestManager getRequestManager() {
        return mRequestManager;
    }
}
