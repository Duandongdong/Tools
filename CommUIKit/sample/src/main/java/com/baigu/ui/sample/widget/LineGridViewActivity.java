package com.baigu.ui.sample.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.ui.sample.R;
import com.baigu.util.log.Logger;

public class LineGridViewActivity extends Activity {

    private GridView mGridView;
    private GridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_grid_view);

        mGridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new GridAdapter(this, mGridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(mAdapter);

    }

    private static final class GridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private Context mContext;
        private GridView mGridView;
        private LineGridItem[] items;

        public GridAdapter(Context context, GridView gridView) {
            mContext = context;
            mGridView = gridView;
            items = LineGridItem.values();
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public LineGridItem getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_main, parent, false);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final LineGridItem item = getItem(position);
            if (item == null) {
                return convertView;
            }

            holder.ivIcon.setImageResource(item.iconRes);
            holder.tvTitle.setText(item.title);

            int gridWidth = mGridView.getWidth();
            int gridHeight = mGridView.getHeight();

            Logger.d("gridWidth:" + gridWidth);
            Logger.d("gridHeight:" + gridHeight);
            Logger.d("gridMeasuredWidth:" + mGridView.getMeasuredWidth());
            Logger.d("gridMeasuredHeight:" + mGridView.getMeasuredHeight());

            // if (gridWidth > 0 && gridHeight > 0) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(gridWidth / 3, gridWidth / 3);
            convertView.setLayoutParams(params);
            // }
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

        public final class ViewHolder {
            public ImageView ivIcon;
            public TextView tvTitle;
        }
    }
}
