package com.bestgood.commons.sample.ui.ptr.loadmore.ui.viewholderdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.base.DemoTitleBaseFragment;
import com.bestgood.commons.sample.ui.ptr.loadmore.datamodel.Images;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.impl.DefaultImageLoadHandler;
import in.srain.cube.views.list.ListViewDataAdapter;

public abstract class ViewHolderDemoBaseFragment extends DemoTitleBaseFragment {

    private ImageLoader mImageLoader;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mImageLoader = ImageLoaderFactory.create(getActivity()).attachToCubeFragment(this);
        ((DefaultImageLoadHandler) mImageLoader.getImageLoadHandler()).setImageRounded(true, 25);

        final View view = inflater.inflate(R.layout.load_small_image, null);

        ListView listView = (ListView) view.findViewById(R.id.load_small_image_list_view);

        ListViewDataAdapter<String> adapter = new ListViewDataAdapter<String>();

        setupViews(adapter);

        listView.setAdapter(adapter);
        adapter.getDataList().addAll(Images.getImages());
        adapter.notifyDataSetChanged();
        return view;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    protected abstract void setupViews(ListViewDataAdapter<String> adapter);
}
