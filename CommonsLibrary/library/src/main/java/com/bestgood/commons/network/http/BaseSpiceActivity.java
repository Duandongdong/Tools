package com.bestgood.commons.network.http;

import android.app.Activity;
import android.os.Bundle;

import com.octo.android.robospice.SpiceManager;

/**
 * This class is the base class of all activities of the sample project. This
 * class offers all subclasses an easy access to a {@link SpiceManager} that is
 * linked to the {@link Activity} lifecycle. Typically, in a new project, you
 * will have to create a base class like this one and copy the content of the
 * {@link BaseSpiceActivity} into your own class.
 *
 * @author ddc
 * @date: Jun 7, 2014 7:31:16 PM
 */
public abstract class BaseSpiceActivity extends Activity {
    private RequestManager mRequestManager = new RequestManager(HttpClientService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestManager.start(this);
    }

    @Override
    protected void onDestroy() {
        mRequestManager.shouldStop();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        // mRequestManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        // mRequestManager.shouldStop();
        super.onStop();
    }

    public RequestManager getRequestManager() {
        return mRequestManager;
    }
}
