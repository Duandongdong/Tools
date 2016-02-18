package com.bestgood.commons.sample.ui.ptr.loadmore.ui.loadmore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.sample.ui.ptr.loadmore.base.DemoTitleBaseFragment;
import com.bestgood.commons.sample.ui.ptr.loadmore.data.ImageListItem;
import com.bestgood.commons.sample.ui.ptr.loadmore.datamodel.ImageListDataModel;
import com.bestgood.commons.sample.ui.ptr.loadmore.event.DemoSimpleEventHandler;
import com.bestgood.commons.sample.ui.ptr.loadmore.event.ErrorMessageDataEvent;
import com.bestgood.commons.sample.ui.ptr.loadmore.event.EventCenter;
import com.bestgood.commons.sample.ui.ptr.loadmore.event.ImageListDataEvent;
import com.bestgood.commons.sample.ui.ptr.loadmore.ui.viewholders.ImageListItemSmallImageViewHolder;
import com.bestgood.commons.ui.ptr.PtrDefaultHandler;
import com.bestgood.commons.ui.ptr.PtrFrameLayout;
import com.bestgood.commons.ui.ptr.PtrHandler;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.list.PagedListViewDataAdapter;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;

public class LoadMoreListViewFragment extends DemoTitleBaseFragment {

    private PagedListViewDataAdapter<ImageListItem> mAdapter;
    private ImageListDataModel mDataModel;
    private ImageLoader mImageLoader;
    private PtrFrameLayout mPtrFrameLayout;
    private ListView mListView;

    @Override
    public View createView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        setHeaderTitle(R.string.cube_demo_load_more_list_view);

        mImageLoader = ImageLoaderFactory.create(getContext()).attachToCubeFragment(this);

        // set up data
        mDataModel = new ImageListDataModel(5);

        mAdapter = new PagedListViewDataAdapter<ImageListItem>();
        mAdapter.setViewHolderClass(this, ImageListItemSmallImageViewHolder.class, mImageLoader);
        mAdapter.setListPageInfo(mDataModel.getListPageInfo());

        // set up views
        final View view = inflater.inflate(R.layout.fragment_load_more_list_view, null);

        // pull to refresh
        mPtrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.load_more_list_view_ptr_frame);

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check list view, not content.
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mDataModel.queryFirstPage();
            }
        });

        // list view
        mListView = (ListView) view.findViewById(R.id.load_more_small_image_list_view);

        // header place holder
        View headerMarginView = new View(getContext());
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
        mListView.addHeaderView(headerMarginView);

        // load more container
        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();

        // binding view and data
        mListView.setAdapter(mAdapter);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                mDataModel.queryNextPage();
            }
        });

        // process data
        EventCenter.bindContainerAndHandler(this, new DemoSimpleEventHandler() {

            public void onEvent(ImageListDataEvent event) {

                // ptr
                mPtrFrameLayout.refreshComplete();

                // load more
                loadMoreListViewContainer.loadMoreFinish(mDataModel.getListPageInfo().isEmpty(), mDataModel.getListPageInfo().hasMore());

                mAdapter.notifyDataSetChanged();
            }

            public void onEvent(ErrorMessageDataEvent event) {
                loadMoreListViewContainer.loadMoreError(0, event.message);
            }

        }).tryToRegisterIfNot();

        // auto load data
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 150);

        return view;
    }
}
