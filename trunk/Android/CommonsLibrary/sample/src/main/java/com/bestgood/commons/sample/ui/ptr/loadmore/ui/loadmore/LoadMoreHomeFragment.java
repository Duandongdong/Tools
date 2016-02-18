package com.bestgood.commons.sample.ui.ptr.loadmore.ui.loadmore;

import android.view.View;
import android.view.View.OnClickListener;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.fragment.DemoBlockMenuFragment;

import java.util.ArrayList;

public class LoadMoreHomeFragment extends DemoBlockMenuFragment {

    @Override
    protected void addItemInfo(ArrayList<MenuItemInfo> itemInfos) {
        itemInfos.add(newItemInfo(R.string.cube_demo_load_more_list_view, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(LoadMoreListViewFragment.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_load_more_grid_view, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(LoadMoreGridViewFragment.class, null);
            }
        }));
    }

    @Override
    protected void setupViews(View view) {
        super.setupViews(view);
        setHeaderTitle(R.string.cube_demo_load_more_demo_title);
    }
}
