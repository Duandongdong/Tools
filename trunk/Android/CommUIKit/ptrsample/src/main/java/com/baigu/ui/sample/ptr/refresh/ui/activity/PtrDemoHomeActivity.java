package com.baigu.ui.sample.ptr.refresh.ui.activity;

import android.os.Bundle;

import com.baigu.ui.sample.ptr.R;
import com.baigu.ui.sample.ptr.refresh.ui.PtrDemoHomeFragment;

import in.srain.cube.mints.base.MintsBaseActivity;

public class PtrDemoHomeActivity extends MintsBaseActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        pushFragmentToBackStack(PtrDemoHomeFragment.class, null);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}