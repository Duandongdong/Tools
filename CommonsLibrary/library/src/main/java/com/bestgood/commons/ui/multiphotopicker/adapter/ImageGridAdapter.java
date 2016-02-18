package com.bestgood.commons.ui.multiphotopicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestgood.commons.R;
import com.bestgood.commons.ui.multiphotopicker.model.ImageItem;
import com.bestgood.commons.ui.multiphotopicker.util.ImageDisplayer;

import java.util.List;


public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageItem> mDataList;

    public ImageGridAdapter(Context context, List<ImageItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.multiphotopicker_list_item_image, null);
            mHolder = new ViewHolder();
            mHolder.imageIv = (ImageView) convertView.findViewById(R.id.image);
            mHolder.selectedIv = (ImageView) convertView.findViewById(R.id.selected_tag);
            mHolder.selectedBgTv = (TextView) convertView.findViewById(R.id.image_selected_bg);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final ImageItem item = mDataList.get(position);

        ImageDisplayer.getInstance(mContext).displayBmp(mHolder.imageIv, item.thumbnailPath, item.sourcePath);

        if (item.isSelected) {
            mHolder.selectedIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.multiphotopicker_tag_selected));
            mHolder.selectedIv.setVisibility(View.VISIBLE);
            mHolder.selectedBgTv.setBackgroundResource(R.drawable.multiphotopicker_image_selected);
        } else {
            mHolder.selectedIv.setImageDrawable(null);
            mHolder.selectedIv.setVisibility(View.GONE);
            mHolder.selectedBgTv.setBackgroundResource(R.color.light_gray);
        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView imageIv;
        private ImageView selectedIv;
        private TextView selectedBgTv;
    }
}
