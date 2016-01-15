package com.baigu.ui.multiphotopicker.event;

/**
 * Created by JIADONG on 2016/1/15.
 * 删除list中已拍摄/选择的照片
 */
public class DeletePictureListEvent {
    public int index;//删除照片在list中的位置，如果小于0 ，全部清除

    public DeletePictureListEvent(int index) {
        this.index = index;
    }
}
