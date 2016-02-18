package com.bestgood.commons.sample.ui.ptr.loadmore.base;

import android.os.Bundle;


import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.datamodel.DemoUploadModel;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.fragment.CubeDemoHomeFragment;

import in.srain.cube.mints.base.MintsBaseActivity;

public class DemoHomeActivity extends MintsBaseActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        pushFragmentToBackStack(CubeDemoHomeFragment.class, null);
        DemoUploadModel.testUpload();
    }

    protected String getCloseWarning() {
        return "Tap back to exit";
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}