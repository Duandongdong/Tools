package com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.base.DemoTitleBaseActivity;
import com.bestgood.commons.sample.ui.ptr.loadmore.datamodel.Images;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.viewholders.StringBigImageViewHolder;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.views.list.ListViewDataAdapter;

public class LoadBigImageInActivity extends DemoTitleBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeaderTitle(R.string.cube_demo_load_big_image_in_activity);
        setContentView(R.layout.load_big_image);

        ImageLoader imageLoader = ImageLoaderFactory.create(this).tryToAttachToContainer(this);

        final View v = mContentContainer;
        ListViewDataAdapter<String> adapter = new ListViewDataAdapter<String>();
        adapter.setViewHolderClass(this, StringBigImageViewHolder.class, imageLoader);
        adapter.getDataList().addAll(Images.getImages());

        ListView listView = (ListView) v.findViewById(R.id.load_big_image_list_view);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
