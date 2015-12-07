package com.baigu.ui.sample.ptr.loadmore.ui.imageloader.activity;

import android.os.Bundle;
import android.widget.ListView;
import com.baigu.ui.sample.ptr.R;
import com.baigu.ui.sample.ptr.loadmore.base.DemoTitleBaseActivity;
import com.baigu.ui.sample.ptr.loadmore.datamodel.Images;
import com.baigu.ui.sample.ptr.loadmore.ui.viewholders.StringSmallImageViewHolder;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.impl.DefaultImageLoadHandler;
import in.srain.cube.views.list.ListViewDataAdapter;

public class LoadSmallImageInActivity extends DemoTitleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeaderTitle(R.string.cube_demo_load_small_image_in_activity);
        setContentView(R.layout.load_small_image);

        ImageLoader imageLoader = ImageLoaderFactory.create(this).tryToAttachToContainer(this);
        ((DefaultImageLoadHandler) imageLoader.getImageLoadHandler()).setImageRounded(true, 25);

        ListView listView = (ListView) mContentContainer.findViewById(R.id.load_small_image_list_view);

        ListViewDataAdapter<String> adapter = new ListViewDataAdapter<String>();
        adapter.setViewHolderClass(this, StringSmallImageViewHolder.class, imageLoader);
        adapter.getDataList().addAll(Images.getImages());

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
