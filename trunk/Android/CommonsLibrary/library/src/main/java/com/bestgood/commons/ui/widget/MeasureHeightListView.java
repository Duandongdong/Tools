
package com.bestgood.commons.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可放在ScrollView中的ListView,计算ListView的高度
 *
 * @author ddc
 * @date: Jul 27, 2014 9:27:17 PM
 */
public class MeasureHeightListView extends ListView {
    public MeasureHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureHeightListView(Context context) {
        super(context);
    }

    public MeasureHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 根据模式计算每个child的高度和宽度
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
