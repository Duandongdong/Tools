package com.bestgood.commons.sample.ui.ptr.loadmore.ui.viewholderdemo;

import com.bestgood.commons.sample.R;

import in.srain.cube.views.list.ListViewDataAdapter;

public class TopLevelClassDemoFragment extends ViewHolderDemoBaseFragment {

    protected void setupViews(ListViewDataAdapter<String> adapter) {
        setHeaderTitle(R.string.cube_demo_view_holder_top_class);

        // top class view holder class
        adapter.setViewHolderClass(this, TopLevelViewHolder.class, getImageLoader());
    }
}
