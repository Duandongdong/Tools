package com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.fragment.DemoBlockMenuFragment;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.activity.LoadBigImageInActivity;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.activity.LoadMidImageInActivity;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.activity.LoadSmallImageInActivity;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.fragment.LoadBigImageFragment;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.fragment.LoadMidImageInFragment;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.fragment.LoadSmallImageInFragment;

import java.util.ArrayList;

public class ImageLoaderHomeFragment extends DemoBlockMenuFragment {

    @Override
    protected void addItemInfo(ArrayList<MenuItemInfo> itemInfos) {
        itemInfos.add(newItemInfo(R.string.cube_demo_load_big_image_in_activity, "#4d90fe", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), LoadBigImageInActivity.class);
                startActivity(intent);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_load_mid_image_in_activity, "#4d90fe", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), LoadMidImageInActivity.class);
                startActivity(intent);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_load_small_image_in_activity, "#4d90fe", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), LoadSmallImageInActivity.class);
                startActivity(intent);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_load_big_image_in_fragment, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(LoadBigImageFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.cube_demo_load_middle_image_in_fragment, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(LoadMidImageInFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.cube_demo_load_small_image_in_fragment, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(LoadSmallImageInFragment.class, null);
            }
        }));

        // pre load
        itemInfos.add(newItemInfo(R.string.cube_demo_pre_load_image, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(PreLoadImageFragment.class, null);
            }
        }));

        // image loader management
        itemInfos.add(newItemInfo(R.string.cube_demo_image_loader_management, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(ImageLoaderManagementFragment.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.cube_demo_customized_image_load_handler, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(ImageLoaderManagementFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.cube_demo_rounded_image, "#4d90fe", new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(RoundedImageFragment.class, null);
            }
        }));
    }

    @Override
    protected void setupViews(View view) {
        setHeaderTitle(R.string.cube_demo_block_image_loader);
        super.setupViews(view);
    }
}
