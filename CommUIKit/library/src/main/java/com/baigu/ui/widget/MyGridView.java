package com.baigu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView{
    private boolean needScrollBar=false;
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
    public MyGridView(Context context) {
		super(context);// TODO Auto-generated constructor stub
	}
	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	 if (!needScrollBar) {     
             int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);     
             super.onMeasure(widthMeasureSpec, expandSpec);     
         } else {     
             super.onMeasure(widthMeasureSpec, heightMeasureSpec);     
         }     
    }
}
