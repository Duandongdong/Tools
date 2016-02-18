package com.bestgood.commons.sample.ui.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.util.ScreenUtils;


public class LineGridLayoutActivity extends Activity {

    private GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_grid_layout);

        mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        addGridItem();
    }

    public void addGridItem() {
        LineGridItem[] items = LineGridItem.values();
//        mGridLayout.setBackgroundColor(Color.parseColor("#FF" + String.format("%06d", new Random().nextInt(999999))));
        for (int i = 0; i < items.length; i++) {

            View convertView = LayoutInflater.from(this).inflate(R.layout.grid_item_main, mGridLayout, false);
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

            LineGridItem item = items[i];
            ivIcon.setImageResource(item.iconRes);
            tvTitle.setText(item.title);

//            ImageView image = new ImageView(this);
//            image.setFocusable(true);
//            image.setClickable(true);
//            image.setBackgroundColor(Color.parseColor("#8F" + String.format("%06d", new Random().nextInt(999999))));
//            image.setScaleType(ImageView.ScaleType.FIT_XY);
//            image.setImageResource(new Random().nextInt() % 2 == 0 ? R.drawable.mayer : R.drawable.mayer1);

            createGridItem(convertView, i);
        }
    }


    private void createGridItem(View view, int index) {
        // int colCount = totalCount / 2;
        int colCount = 3;
        int row = index / colCount;
        int col = index % colCount;

        int margin = 0;

        String tag = "index(" + mGridLayout.getChildCount() + ")" + "  " + row + "," + col;
        view.setTag(tag);// test....
        view.setBackgroundResource(android.R.drawable.list_selector_background);
        view.setClickable(true);

        float gridItemSize = (float) ScreenUtils.getScreenWidthPixels(this) / (float) colCount;
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                new ViewGroup.LayoutParams((int) gridItemSize, (int) gridItemSize));

        params.columnSpec = GridLayout.spec(col, 1, GridLayout.CENTER);
        params.rowSpec = GridLayout.spec(row, 1, GridLayout.CENTER);
        params.setGravity(Gravity.TOP);
        params.setMargins(margin, margin, margin, margin);

        mGridLayout.addView(view, params);
    }
}
