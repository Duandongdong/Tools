package com.baigu.ui.sample.ptr.loadmore.ui.request;

import android.view.View;
import android.view.View.OnClickListener;
import com.baigu.ui.sample.ptr.R;
import com.baigu.ui.sample.ptr.loadmore.ui.fragment.DemoBlockMenuFragment;

import java.util.ArrayList;

public class RequestHomeFragment extends DemoBlockMenuFragment {

    @Override
    protected void addItemInfo(ArrayList<MenuItemInfo> itemInfos) {
        itemInfos.add(newItemInfo(R.string.cube_demo_request_simple_api, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(RequestDemoFragment.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_request_cache_able_request, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(CacheAbleRequestDemoFragment.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_request_cache_management, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(RequestCacheManagementFragment.class, null);
            }
        }));
    }

    @Override
    protected void setupViews(View view) {
        setHeaderTitle(R.string.cube_demo_request_demo);
        super.setupViews(view);
    }
}
