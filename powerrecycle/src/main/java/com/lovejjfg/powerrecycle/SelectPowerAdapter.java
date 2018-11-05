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

package com.lovejjfg.powerrecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.lovejjfg.powerrecycle.annotation.SelectMode;
import com.lovejjfg.powerrecycle.model.ISelect;
import java.util.HashSet;

/**
 * Created by Joe on 2016-03-11
 * Email: lovejjfg@gmail.com
 */

/**
 * {@link SelectPowerAdapter} impl SelectMode,you can call  {@link #setSelectedMode(int)} to switch
 * {@link ISelect#SINGLE_MODE} or {@link ISelect#MULTIPLE_MODE}
 * and you can decide whether it's enableSelect the longTouch to jump to  SelectMode, you can call
 * {@link #longTouchSelectModeEnable(boolean)} to change ,by the way,the default was disable
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public abstract class SelectPowerAdapter<T extends ISelect> extends PowerAdapter<T> {

    private int currentMode;
    private int prePos = -1;
    private int defaultPos = -1;
    private int currentPos = -1;
    private boolean longTouchEnable;
    protected boolean isSelectMode;
    @Nullable
    private
    OnItemSelectedListener selectedListener;

    public HashSet<T> getSelectedBeans() {
        return selectedBeans;
    }

    private final HashSet<T> selectedBeans = new HashSet<>();

    public void updateSelectMode(boolean isSelect) {
        if (isSelectMode != isSelect) {
            isSelectMode = isSelect;
            resetAll();
            notifyDataSetChanged();
        }
    }

    public void setDefaultSelectedPos(int position) {
        this.defaultPos = position;
    }

    public void setCurrentPos(int position) {
        if (list.isEmpty() || position > list.size() - 1 || position < 0) {
            return;
        }
        if (currentPos != position) {
            currentPos = position;
            if (prePos == -1) {
                prePos = currentPos;
            }
            T t = list.get(position);
            if (!t.isSelected()) {
                resetAll();
                t.setSelected(true);
                notifyItemChanged(position);
            }
        }
    }

    public SelectPowerAdapter(@SelectMode int currentMode, boolean longTouchEnable) {
        this.currentMode = currentMode;
        this.longTouchEnable = longTouchEnable;
        this.isSelectMode = true;
    }

    public void resetAll() {
        for (T iSelect : list) {
            if (iSelect.isSelected()) {
                iSelect.setSelected(false);
                selectedBeans.remove(iSelect);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    public void longTouchSelectModeEnable(boolean longTouchSelectModeEnable) {
        longTouchEnable = longTouchSelectModeEnable;
    }

    public void setSelectedMode(@SelectMode int mode) {
        currentMode = mode;
    }

    @Override
    public void performClick(@NonNull final View itemView, final int position, T iSelect) {

        if (isSelectMode) {
            if (currentMode == ISelect.SINGLE_MODE && iSelect.isSelected() && defaultPos != -1) {
                return;
            }
            boolean selected = !iSelect.isSelected();
            iSelect.setSelected(selected);
            dispatchSelected(itemView, position, iSelect, selected);
            if (currentMode == ISelect.SINGLE_MODE && prePos != -1 && position != prePos && iSelect.isSelected()) {
                list.get(prePos).setSelected(false);
                dispatchSelected(itemView, prePos, iSelect, false);
                notifyItemChanged(prePos);
            }
            notifyDataSetChanged();
            prePos = position;
        } else {
            if (clickListener != null) {
                clickListener.onItemClick(itemView, position, iSelect);
            }
        }
    }

    private void dispatchSelected(View itemView, int position, T iSelect, boolean isSelected) {
        if (isSelected) {
            selectedBeans.add(iSelect);
        } else {
            selectedBeans.remove(iSelect);
            if (selectedListener != null && selectedBeans.isEmpty()) {
                selectedListener.onNothingSelected();
            }
        }
        if (selectedListener != null) {
            selectedListener.onItemSelected(itemView, position, isSelected);
        }
    }

    @Override
    public boolean performLongClick(@NonNull View itemView, int position, T iSelect) {
        if (longTouchEnable) {
            updateSelectMode(true);
            iSelect.setSelected(!iSelect.isSelected());
            dispatchSelected(itemView, position, iSelect, iSelect.isSelected());
            notifyItemChanged(position);
            prePos = position;
            return true;
        } else {
            return super.performLongClick(itemView, position, iSelect);
        }
    }

    public void setOnItemSelectListener(OnItemSelectedListener listener) {
        this.selectedListener = listener;
    }

    public void selectAll() {
        if (!list.isEmpty()) {
            for (T bean : list) {
                bean.setSelected(true);
                selectedBeans.add(bean);
            }
        }
        notifyDataSetChanged();
    }
}
