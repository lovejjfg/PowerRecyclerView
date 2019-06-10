/*
 * Copyright (c) 2016.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.powerrecyclerx.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joe on 2016/11/17.
 * Email lovejjfg@gmail.com
 */

@SuppressWarnings("unused")
public class AbsSelect implements ISelect, Parcelable {
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

    protected AbsSelect() {
    }

    @SuppressWarnings("WeakerAccess")
    protected AbsSelect(Parcel in) {
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<AbsSelect> CREATOR = new Creator<AbsSelect>() {
        @Override
        public AbsSelect createFromParcel(Parcel source) {
            return new AbsSelect(source);
        }

        @Override
        public AbsSelect[] newArray(int size) {
            return new AbsSelect[size];
        }
    };
}
