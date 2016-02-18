package com.bestgood.commons.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import com.bestgood.commons.R;


/**
 * Created by dengdingchun on 15/10/27.
 * <p/>
 * 画分割线的GridLayout
 */
public class LineGridLayout extends GridLayout {

    public LineGridLayout(Context context) {
        super(context);
    }

    public LineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View localView1 = getChildAt(0);
        if (localView1 != null && localView1.getWidth() > 0) {

            int childCount = getChildCount();
            Object childCountTag = getTag(R.id.grid_layout_child_count);
            if (childCountTag != null && childCountTag instanceof Integer) {
                childCount = (int) childCountTag;
            }

            int column = getColumnCount();
            //int row = getRowCount();

            Paint localPaint = new Paint();
            localPaint.setAntiAlias(true);
            localPaint.setStrokeWidth(0);
            localPaint.setColor(Color.parseColor("#FFE5E5E5"));
            for (int i = 0; i < childCount; i++) {
                View cellView = getChildAt(i);
                // 左边画一条竖线
                canvas.drawLine(
                        cellView.getLeft(),
                        cellView.getTop(),
                        cellView.getLeft(),
                        cellView.getBottom(),
                        localPaint);

                //上边画一条横线线
                canvas.drawLine(
                        cellView.getLeft(),
                        cellView.getTop(),
                        cellView.getRight(),
                        cellView.getTop(),
                        localPaint);

                // 右边画一条竖线
                if (i + 1 % column == 0 || childCount < i + column + 1) {
                    canvas.drawLine(
                            cellView.getRight(),
                            cellView.getTop(),
                            cellView.getRight(),
                            cellView.getBottom(),
                            localPaint);
                }

                // 底边画一条横线线
                if (childCount < i + column + 1) {
                    canvas.drawLine(
                            cellView.getLeft(),
                            cellView.getBottom(),
                            cellView.getRight(),
                            cellView.getBottom(),
                            localPaint);
                }
            }
        }
    }
}
