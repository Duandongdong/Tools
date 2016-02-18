package com.bestgood.commons.sample.ui.ptr.loadmore.ui.localcache;

import android.view.View;
import android.view.View.OnClickListener;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.fragment.DemoBlockMenuFragment;

import java.util.ArrayList;

public class LocalCacheHomeFragment extends DemoBlockMenuFragment {

    @Override
    protected void addItemInfo(ArrayList<MenuItemInfo> itemInfos) {
        itemInfos.add(newItemInfo(R.string.cube_demo_local_cache, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(LocalCacheFragment.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_local_cache_management, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(LocalCacheManagementFragment.class, null);
            }
        }));
    }

    @Override
    protected void setupViews(View view) {
        setHeaderTitle(R.string.cube_demo_local_cache);
        super.setupViews(view);
    }
}
