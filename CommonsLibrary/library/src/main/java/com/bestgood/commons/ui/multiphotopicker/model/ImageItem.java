package com.bestgood.commons.ui.multiphotopicker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * 图片对象
 */
public class ImageItem implements Parcelable {

    @Expose
    public String imageId;
    @Expose
    public String thumbnailPath;
    @Expose
    public String sourcePath;
    @Expose
    public boolean isSelected = false;

    public ImageItem() {
    }

    protected ImageItem(Parcel in) {
        imageId = in.readString();
        thumbnailPath = in.readString();
        sourcePath = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sourcePath == null) ? 0 : sourcePath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImageItem other = (ImageItem) obj;
        if (sourcePath == null) {
            if (other.sourcePath != null)
                return false;
        } else if (!sourcePath.equals(other.sourcePath))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageId);
        dest.writeString(thumbnailPath);
        dest.writeString(sourcePath);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
