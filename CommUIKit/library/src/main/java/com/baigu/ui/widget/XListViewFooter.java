/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */

package com.baigu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baigu.ui.R;


public class XListViewFooter extends LinearLayout {

	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private String mHintNormal;
	private String mHintReady;
	private String mHintLoading;

	private Context mContext;
	private View mContentView;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mHintTextView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);

		mArrowImageView = (ImageView) findViewById(R.id.xlistview_footer_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_footer_hint_textview);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_footer_progressbar);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);

		mHintNormal = context.getResources().getString(
				R.string.xlistview_header_hint_normal);
		mHintReady = context.getResources().getString(
				R.string.xlistview_header_hint_ready);
		mHintLoading = context.getResources().getString(
				R.string.xlistview_loading);
	}

	public void setState(int state) {
		if (state == mState) {
			return;
		}
		if (state == STATE_LOADING) { // 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else { // 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_LOADING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(mHintNormal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(mHintReady);
			}
			break;
		case STATE_LOADING:
			mProgressBar.setVisibility(View.VISIBLE);
			mHintTextView.setText(mHintLoading);
			break;
		default:
		}
		mState = state;
	}

	public void setBottomMargin(int height) {
		if (height < 0) {
			return;
		}
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintTextView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintTextView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	public void setHintNormal(String hintNormal) {
		this.mHintNormal = hintNormal;
		mHintTextView.setText(mHintNormal);
	}

	public void setHintReady(String hintReady) {
		this.mHintReady = hintReady;
	}

	public void setHintLoading(String hintLoading) {
		this.mHintLoading = hintLoading;
	}
}
