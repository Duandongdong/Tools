package com.bestgood.commons.sample.ui.widget;

import com.bestgood.commons.sample.R;

/**
 * Created by dengdingchun on 15/10/27.
 */
public enum LineGridItem {
    MESSAGE(R.drawable.ic_line_grid_message, "消息"), //
    RANKING(R.drawable.ic_line_grid_ranking, "排名"), //
    ADDR(R.drawable.ic_line_grid_addr, "地址管理"), //
    POINT(R.drawable.ic_line_grid_point, "积分"), //
    ORDERS(R.drawable.ic_line_grid_orders, "订单查询"), //
    SETTING(R.drawable.ic_line_grid_setting, "设置"), //
    TIPS(R.drawable.ic_line_grid_tips, "社区");

    public int iconRes;
    public String title;

    LineGridItem(int iconRes, String title) {
        this.iconRes = iconRes;
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
