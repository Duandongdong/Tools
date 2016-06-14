package com.bestgood.commons.ui.multiphotopicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bestgood.commons.R;
import com.bestgood.commons.ui.multiphotopicker.model.ImageItem;
import com.bestgood.commons.ui.multiphotopicker.util.CustomConstants;
import com.bestgood.commons.ui.multiphotopicker.util.ImageDisplayer;
import com.bestgood.commons.util.ScreenUtils;
import com.bestgood.commons.util.SizeUtils;
import com.bestgood.commons.util.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class ImagePublishAdapter extends BaseAdapter {
    private List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private Context mContext;
    private int itemWidth;
    private int itemHeight;

    public ImagePublishAdapter(Context context, List<ImageItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;

        float screenWidth = ScreenUtils.getScreenWidthPixels(mContext) - SizeUtils.dip2px(mContext, 10);
        float width = screenWidth / 3;
        float height = width * 9f / 16f;

        itemWidth = (int) width;
        itemHeight = (int) height;
    }

    public int getCount() {
        // 多返回一个用于展示添加图标
        if (mDataList == null) {
            return 1;
        } else if (mDataList.size() == CustomConstants.MAX_IMAGE_SIZE) {
            return CustomConstants.MAX_IMAGE_SIZE;
        } else {
            return mDataList.size() + 1;
        }
    }

    public Object getItem(int position) {
        if (mDataList != null && mDataList.size() == CustomConstants.MAX_IMAGE_SIZE) {
            return mDataList.get(position);
        } else if (mDataList == null || position - 1 < 0 || position > mDataList.size()) {
            return null;
        } else {
            return mDataList.get(position - 1);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        // 所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
        convertView = LayoutInflater.from(mContext).inflate(R.layout.multiphotopicker_item_publish, parent, false);

        ImageView imageIv = (ImageView) convertView.findViewById(R.id.item_grid_image);
        ViewGroup.LayoutParams params = imageIv.getLayoutParams();
        params.width = itemWidth;
        params.height = itemHeight;

        Logger.e("width:%d", params.width);
        Logger.e("height:%d", params.height);
        Logger.object(params);

        if (isShowAddItem(position)) {
            imageIv.setImageResource(R.drawable.multiphotopicker_btn_add_pic);
            imageIv.setBackgroundResource(R.color.bg_gray);
        } else {
            final ImageItem item = mDataList.get(position);
            ImageDisplayer.getInstance(mContext).displayBmp(imageIv, item.thumbnailPath, item.sourcePath);
        }

        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        return position == size;
    }
}