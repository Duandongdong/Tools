package com.baigu.ui.sample.ptr.loadmore.ui.imageloader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.baigu.ui.sample.ptr.R;
import com.baigu.ui.sample.ptr.loadmore.base.DemoTitleBaseFragment;
import com.baigu.ui.sample.ptr.loadmore.datamodel.Images;
import com.baigu.ui.sample.ptr.loadmore.ui.viewholders.StringSmallImageViewHolder;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.impl.DefaultImageLoadHandler;
import in.srain.cube.views.list.ListViewDataAdapter;

public class LoadSmallImageInFragment extends DemoTitleBaseFragment {

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHeaderTitle(R.string.cube_demo_load_small_image_in_fragment);

        ImageLoader imageLoader = ImageLoaderFactory.create(getActivity()).attachToCubeFragment(this);
        ((DefaultImageLoadHandler) imageLoader.getImageLoadHandler()).setImageRounded(true, 25);

        final View v = inflater.inflate(R.layout.load_small_image, null);

        ListView listView = (ListView) v.findViewById(R.id.load_small_image_list_view);

        ListViewDataAdapter<String> adapter = new ListViewDataAdapter<String>();
        adapter.setViewHolderClass(this, StringSmallImageViewHolder.class, imageLoader);
        adapter.getDataList().addAll(Images.getImages());

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return v;
    }
}
