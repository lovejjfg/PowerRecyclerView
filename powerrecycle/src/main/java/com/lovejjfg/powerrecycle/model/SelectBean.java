package com.lovejjfg.powerrecycle.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joe on 2016/11/17.
 * Email lovejjfg@gmail.com
 */

public class SelectBean implements ISelect, Parcelable {
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public SelectBean() {
    }

    protected SelectBean(Parcel in) {
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SelectBean> CREATOR = new Parcelable.Creator<SelectBean>() {
        @Override
        public SelectBean createFromParcel(Parcel source) {
            return new SelectBean(source);
        }

        @Override
        public SelectBean[] newArray(int size) {
            return new SelectBean[size];
        }
    };
}
