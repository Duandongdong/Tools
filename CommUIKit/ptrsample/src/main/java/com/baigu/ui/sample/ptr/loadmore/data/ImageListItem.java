package com.baigu.ui.sample.ptr.loadmore.data;

import in.srain.cube.request.JsonData;

public class ImageListItem {

    public String picUrl;

    public ImageListItem(JsonData jsonData) {
        picUrl = jsonData.optString("pic");
    }
}
