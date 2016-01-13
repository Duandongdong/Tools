package com.baigu.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.ui.R;


/**
 * @author ddc
 * @date: Jul 5, 2014 2:30:29 PM
 */
public class PopupSpinnerView extends TextView {
    private PopupWindow mPopupWindow;
    private ListView mListView;
    private BaseAdapter mAdapter;

    private int mSelectedPosition;

    private OnClickListener mOnClickListener = null;
    private OnItemClickListener mOnItemClickListener;
    private OnItemSelectedListener mOnItemSelectedListener;

    private OnPopupWindowChangeListener mOnPopupWindowChangeListener;

    private View mShowAsDropDownView;

    // ===============================================================================
    public PopupSpinnerView(Context context) {
        this(context, null);
    }

    public PopupSpinnerView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public PopupSpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // ===============================================================================
    private void init() {
        super.setOnClickListener(OnSpinnerClickListener);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (adapter != null) {
            this.mAdapter = adapter;
            if (mListView == null) {
                mListView = new ListView(getContext());
            }
            mListView.setVerticalScrollBarEnabled(false);
            mListView.setCacheColorHint(0);
            mListView.setBackgroundColor(getResources().getColor(
                    R.color.bg_poi_search_spinner));
            // mListView.setBackgroundDrawable(listDrawable);
            mListView.setDivider(new ColorDrawable(getResources().getColor(
                    R.color.divide_line_color_light)));
            mListView.setDividerHeight(1);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(ItemClickListener);
            mListView.setOnItemSelectedListener(ItemSelectedListener);
            MarginLayoutParams params = new MarginLayoutParams(getWidth(),
                    LayoutParams.WRAP_CONTENT);
            int padding = getResources().getDimensionPixelSize(
                    R.dimen.content_edge_padding);
            params.leftMargin = padding;
            params.rightMargin = padding;
            mListView.setPadding(padding, 0, padding, 0);
            mListView.setLayoutParams(params);
            mSelectedPosition = 0;
        } else {
            mSelectedPosition = -1;
            this.mAdapter = null;
        }
        refreshView();
    }

    private void refreshView() {
        if (mAdapter != null) {
            Object popupListItem = mAdapter.getItem(mSelectedPosition);
            setText(popupListItem.toString());
            mAdapter.notifyDataSetChanged();
            // if (popupListItem.getResId() != -1)
            // setCompoundDrawablesWithIntrinsicBounds(
            // popupListItem.getResId(), 0, 0, 0);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(
            OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    public void setOnPopupWindowChangeListener(
            OnPopupWindowChangeListener onPopupWindowChangeListener) {
        this.mOnPopupWindowChangeListener = onPopupWindowChangeListener;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
        refreshView();
    }

    public void setShowAsDropDownView(View showAsDropDownView) {
        this.mShowAsDropDownView = showAsDropDownView;
        mShowAsDropDownView.setOnClickListener(OnSpinnerClickListener);
    }

    public int getListViewPaddingLeft() {
        return mListView == null ? 0 : mListView.getPaddingLeft();
    }

    /**
     * Spinner TextView 点击 响应
     **/
    private final OnClickListener OnSpinnerClickListener = new OnClickListener() {
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            if (mPopupWindow == null || !mPopupWindow.isShowing()) {
                mPopupWindow = new PopupWindow(mListView);
                mPopupWindow.setAnimationStyle(R.style.Animation_dropdown);
                mPopupWindow.setWidth(mShowAsDropDownView == null ? getMeasuredWidth()
                        : mShowAsDropDownView.getMeasuredWidth());
                mPopupWindow.setHeight(getTotalHeightofListView());
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setClippingEnabled(true);
                mPopupWindow.setOnDismissListener(OnPopupWindowDismissListener);
                // mPopupWindow.showAsDropDown(v, v.getLeft(), v.getTop() + 3);
                mPopupWindow.showAsDropDown(mShowAsDropDownView == null ? v
                        : mShowAsDropDownView, 0, 0);
            }
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
            if (mOnPopupWindowChangeListener != null) {
                mOnPopupWindowChangeListener.onOpened(PopupSpinnerView.this);
            }
        }
    };
    // =================================================================================
    /**
     * PopupWindow 关闭监听
     */
    private final PopupWindow.OnDismissListener OnPopupWindowDismissListener = new PopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {
            mPopupWindow = null;
            if (mOnPopupWindowChangeListener != null) {
                mOnPopupWindowChangeListener.onDismiss(PopupSpinnerView.this);
            }
        }
    };

    /**
     * PopupWindow ListView item 点击 监听
     */
    private final OnItemClickListener ItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
            mSelectedPosition = position;
            refreshView();
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(parent, view, position, id);
            }
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(parent, view, position,
                        id);
            }
        }
    };

    private final OnItemSelectedListener ItemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(parent, view, position,
                        id);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onNothingSelected(parent);
            }
        }
    };

    // =================================================================================

    public interface OnPopupWindowChangeListener {

        public void onOpened(PopupSpinnerView v);

        public void onDismiss(PopupSpinnerView v);
    }

    private int getTotalHeightofListView() {

        int totalHeight = 0;
        if (mListView == null || mListView.getAdapter() == null) {
            return totalHeight;
        }
        ListAdapter adapter = mListView.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            View mView = adapter.getView(i, null, mListView);
            mView.measure(MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
            totalHeight += mView.getMeasuredHeight()
                    + mListView.getDividerHeight();
        }
        return totalHeight;
    }
}
