package com.bestgood.commons.sample.ui.ptr.loadmore.ui.viewholderdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.imageloader.ImageSize;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.views.list.ListViewDataAdapter;
import in.srain.cube.views.list.ViewHolderBase;

public class InnerInstanceClassNoArgsFragment extends ViewHolderDemoBaseFragment {

    protected void setupViews(ListViewDataAdapter<String> adapter) {
        setHeaderTitle(R.string.cube_demo_view_holder_inner_instance_no_args_class);

        // no args
        adapter.setViewHolderClass(this, ClassInnerInstanceViewHolder.class);
    }

    /**
     * inner class, initiated with no arguments
     */
    private class ClassInnerInstanceViewHolder extends ViewHolderBase<String> {

        private CubeImageView mImageView;

        @Override
        public View createView(LayoutInflater inflater) {
            View v = inflater.inflate(R.layout.load_small_image_list_item, null);
            mImageView = (CubeImageView) v.findViewById(R.id.load_small_image_list_item_image_view);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return v;
        }

        @Override
        public void showData(int position, String itemData) {
            mImageView.loadImage(getImageLoader(), itemData, ImageSize.sSmallImageReuseInfo);
        }
    }
}
