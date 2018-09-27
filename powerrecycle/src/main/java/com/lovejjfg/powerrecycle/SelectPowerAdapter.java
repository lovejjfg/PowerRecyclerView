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
 * and you can decide whether it's enable the longTouch to jump to  SelectMode, you can call
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
        for (T bean : list) {
            if (bean.isSelected()) {
                bean.setSelected(false);
                selectedBeans.remove(bean);
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
    public void performClick(@NonNull final View itemView, final int position, T t) {
        final T selectBean = list.get(position);

        if (isSelectMode) {
            if (currentMode == ISelect.SINGLE_MODE && selectBean.isSelected() && defaultPos != -1) {
                return;
            }
            boolean selected = !selectBean.isSelected();
            selectBean.setSelected(selected);
            dispatchSelected(itemView, position, selectBean, selected);
            if (currentMode == ISelect.SINGLE_MODE && prePos != -1 && position != prePos && selectBean.isSelected()) {
                list.get(prePos).setSelected(false);
                dispatchSelected(itemView, prePos, selectBean, false);
                notifyItemChanged(prePos);
            }
            notifyDataSetChanged();
            prePos = position;
        } else {
            if (clickListener != null) {
                clickListener.onItemClick(itemView, position, t);
            }
        }
    }

    private void dispatchSelected(View itemView, int position, T testBean, boolean isSelected) {
        if (isSelected) {
            selectedBeans.add(testBean);
        } else {
            selectedBeans.remove(testBean);
            if (selectedListener != null && selectedBeans.isEmpty()) {
                selectedListener.onNothingSelected();
            }
        }
        if (selectedListener != null) {
            selectedListener.onItemSelected(itemView, position, isSelected);
        }
    }

    @Override
    public boolean performLongClick(@NonNull View itemView, int position, T t) {
        if (longTouchEnable) {
            final T testBean = list.get(position);
            updateSelectMode(true);
            testBean.setSelected(!testBean.isSelected());
            dispatchSelected(itemView, position, testBean, testBean.isSelected());
            notifyItemChanged(position);
            prePos = position;
            return true;
        } else {
            return super.performLongClick(itemView, position, t);
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
