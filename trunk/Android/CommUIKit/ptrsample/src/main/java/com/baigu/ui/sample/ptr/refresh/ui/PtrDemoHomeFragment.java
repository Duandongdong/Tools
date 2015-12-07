package com.baigu.ui.sample.ptr.refresh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.baigu.ui.ptr.PtrDefaultHandler;
import com.baigu.ui.ptr.PtrFrameLayout;
import com.baigu.ui.ptr.PtrHandler;
import com.baigu.ui.ptr.header.StoreHouseHeader;
import com.baigu.ui.sample.ptr.R;
import com.baigu.ui.sample.ptr.refresh.ui.classic.AutoRefresh;
import com.baigu.ui.sample.ptr.refresh.ui.classic.EvenOnlyATextView;
import com.baigu.ui.sample.ptr.refresh.ui.classic.HideHeader;
import com.baigu.ui.sample.ptr.refresh.ui.classic.KeepHeader;
import com.baigu.ui.sample.ptr.refresh.ui.classic.PullToRefresh;
import com.baigu.ui.sample.ptr.refresh.ui.classic.ReleaseToRefresh;
import com.baigu.ui.sample.ptr.refresh.ui.classic.WithGridView;
import com.baigu.ui.sample.ptr.refresh.ui.classic.WithListView;
import com.baigu.ui.sample.ptr.refresh.ui.classic.WithListViewAndEmptyView;
import com.baigu.ui.sample.ptr.refresh.ui.classic.WithScrollView;
import com.baigu.ui.sample.ptr.refresh.ui.classic.WithTextViewInFrameLayoutFragment;
import com.baigu.ui.sample.ptr.refresh.ui.classic.WithWebView;
import com.baigu.ui.sample.ptr.refresh.ui.storehouse.StoreHouseUsingPointList;
import com.baigu.ui.sample.ptr.refresh.ui.storehouse.StoreHouseUsingString;
import com.baigu.ui.sample.ptr.refresh.ui.storehouse.StoreHouseUsingStringArray;
import com.baigu.ui.sample.ptr.refresh.ui.viewpager.ViewPagerActivity;

import in.srain.cube.mints.base.BlockMenuFragment;
import in.srain.cube.util.LocalDisplay;

import java.util.ArrayList;

public class PtrDemoHomeFragment extends BlockMenuFragment {

    @Override
    protected void addItemInfo(ArrayList<BlockMenuFragment.MenuItemInfo> itemInfos) {

        // GridView
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_grid_view, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(WithGridView.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_frame_layout, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(WithTextViewInFrameLayoutFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_only_text_view, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(EvenOnlyATextView.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_list_view, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(WithListView.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_web_view, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(WithWebView.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.ptr_demo_block_with_list_view_and_empty_view, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(WithListViewAndEmptyView.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.ptr_demo_block_scroll_view, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(WithScrollView.class, null);
            }
        }));
        itemInfos.add(null);
        itemInfos.add(null);

        itemInfos.add(newItemInfo(R.string.ptr_demo_block_keep_header, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(KeepHeader.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_hide_header, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(HideHeader.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.ptr_demo_block_release_to_refresh, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(ReleaseToRefresh.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.ptr_demo_block_pull_to_refresh, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(PullToRefresh.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_auto_fresh, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(AutoRefresh.class, null);
            }
        }));
        itemInfos.add(null);

        itemInfos.add(newItemInfo(R.string.ptr_demo_block_storehouse_header_using_string_array, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(StoreHouseUsingStringArray.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_storehouse_header_using_string, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(StoreHouseUsingString.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_storehouse_header_using_point_list, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(StoreHouseUsingPointList.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_material_style, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(MaterialStyleFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_material_style_pin_content, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(MaterialStylePinContentFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_with_long_press, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(WithLongPressFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_block_with_view_pager, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewPagerActivity.class);
                startActivity(intent);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_rentals_style, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(RentalsStyleFragment.class, null);
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_enable_next_ptr_at_once, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getContext().pushFragmentToBackStack(EnableNextPTRAtOnce.class, null);
            }
        }));

        itemInfos.add(newItemInfo(R.string.ptr_demo_placeholder, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_placeholder, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        }));
        itemInfos.add(newItemInfo(R.string.ptr_demo_placeholder, R.color.cube_mints_4d90fe, new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        }));
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.createView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.cube_mints_333333));

        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.fragment_ptr_home_ptr_frame);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, LocalDisplay.dp2px(20), 0, LocalDisplay.dp2px(20));
        header.initWithString("Ultra PTR");

        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmengt_ptr_home;
    }

    @Override
    protected void setupViews(View view) {
        super.setupViews(view);
        setHeaderTitle(R.string.ptr_demo_block_for_home);
    }
}
