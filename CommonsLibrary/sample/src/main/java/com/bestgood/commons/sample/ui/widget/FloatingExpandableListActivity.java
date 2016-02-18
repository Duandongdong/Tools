package com.bestgood.commons.sample.ui.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.ui.widget.FloatingWrapperExpandableListAdapter;
import com.bestgood.commons.ui.widget.XExpandableListView;


public class FloatingExpandableListActivity extends Activity implements XExpandableListView.IXListViewListener {

    XExpandableListView mExpandableListView;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_floating_expandablelist);

        mExpandableListView = (XExpandableListView) findViewById(R.id.sample_activity_list);

        mExpandableListView.setGroupIndicator(null);
//        mExpandableListView.setChildIndicator(null);
//        mExpandableListView.setChildDivider(getResources().getDrawable(android.R.color.white));
//        mExpandableListView.setDivider(getResources().getDrawable(android.R.color.white));
//        mExpandableListView.setDividerHeight(1);

        mExpandableListView.setPullLoadEnable(true);
        mExpandableListView.setPullRefreshEnable(true);
        mExpandableListView.setXListViewListener(this);

        // Even though the child divider has already been set on the layout file, we have to set it again here
        // This prevents a bug where the background turns to the color of the child divider when the list is expanded
        mExpandableListView.setChildDivider(new ColorDrawable(Color.BLACK));

        final FloatingExpandableListAdapter adapter = new FloatingExpandableListAdapter(this);
        final FloatingWrapperExpandableListAdapter wrapperAdapter = new FloatingWrapperExpandableListAdapter(adapter);
        mExpandableListView.setAdapter(wrapperAdapter);

        for (int i = 0; i < wrapperAdapter.getGroupCount(); i++) {
            mExpandableListView.expandGroup(i);
        }
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Toast.makeText(getBaseContext(), "groupPosition:" + groupPosition, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getBaseContext(), "groupPosition:" + groupPosition + ";childPosition:" + childPosition, Toast.LENGTH_LONG).show();
                return true;
            }
        });

//        mExpandableListView.setOnScrollFloatingGroupListener(new FloatingGroupExpandableListView.OnScrollFloatingGroupListener() {
//
//            @Override
//            public void onScrollFloatingGroupListener(View floatingGroupView, int scrollY) {
//                float interpolation = -scrollY / (float) floatingGroupView.getHeight();
//
//                // Changing from RGB(162,201,85) to RGB(255,255,255)
//                final int greenToWhiteRed = (int) (162 + 93 * interpolation);
//                final int greenToWhiteGreen = (int) (201 + 54 * interpolation);
//                final int greenToWhiteBlue = (int) (85 + 170 * interpolation);
//                final int greenToWhiteColor = Color.argb(255, greenToWhiteRed, greenToWhiteGreen, greenToWhiteBlue);
//
//                // Changing from RGB(255,255,255) to RGB(0,0,0)
//                final int whiteToBlackRed = (int) (255 - 255 * interpolation);
//                final int whiteToBlackGreen = (int) (255 - 255 * interpolation);
//                final int whiteToBlackBlue = (int) (255 - 255 * interpolation);
//                final int whiteToBlackColor = Color.argb(255, whiteToBlackRed, whiteToBlackGreen, whiteToBlackBlue);
//
//                final ImageView image = (ImageView) floatingGroupView.findViewById(R.id.sample_activity_list_group_item_image);
//                image.setBackgroundColor(greenToWhiteColor);
//
//                final Drawable imageDrawable = image.getDrawable().mutate();
//                imageDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP);
//
//                final View background = floatingGroupView.findViewById(R.id.sample_activity_list_group_item_background);
//                background.setBackgroundColor(greenToWhiteColor);
//
//                final TextView text = (TextView) floatingGroupView.findViewById(R.id.sample_activity_list_group_item_text);
//                text.setTextColor(whiteToBlackColor);
//
//                final ImageView expanded = (ImageView) floatingGroupView.findViewById(R.id.sample_activity_list_group_expanded_image);
//                final Drawable expandedDrawable = expanded.getDrawable().mutate();
//                expandedDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP);
//            }
//        });
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                start = ++refreshCnt;
//                items.clear();
//                geneItems();
//                // mAdapter.notifyDataSetChanged();
//                mAdapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.list_item_xlistview, items);
//                mListView.setAdapter(mAdapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                geneItems();
//                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    private void onLoad() {
        mExpandableListView.stopRefresh();
        mExpandableListView.stopLoadMore();
        mExpandableListView.setRefreshTime("刚刚");
    }
}
