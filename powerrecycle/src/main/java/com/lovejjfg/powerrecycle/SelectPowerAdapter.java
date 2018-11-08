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
import com.lovejjfg.powerrecycle.holder.PowerHolder;
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
    private boolean hasDefaultSelect;
    private boolean longTouchEnable;
    protected boolean isSelectMode;
    @Nullable
    private OnItemSelectedListener selectedListener;

    public HashSet<T> getSelectedBeans() {
        return selectedBeans;
    }

    private final HashSet<T> selectedBeans = new HashSet<>();

    public void updateSelectMode(boolean isSelect) {
        if (isSelectMode != isSelect) {
            isSelectMode = isSelect;
            prePos = -1;
            resetAll();
            notifyDataSetChanged();
        }
    }

    public void setDefaultSelect(boolean hasDefault) {
        this.hasDefaultSelect = hasDefault;
    }

    public void setCurrentPos(int position) {
        if (list.isEmpty() || position > list.size() - 1 || position < 0) {
            return;
        }
        if (prePos != position) {
            prePos = position;
            T t = list.get(position);
            if (!t.isSelected()) {
                resetAll();
                select(t);
                notifyItemChanged(position);
            }
        }
    }

    public SelectPowerAdapter(@SelectMode int currentMode, boolean longTouchEnable) {
        this.currentMode = currentMode;
        this.longTouchEnable = longTouchEnable;
        this.isSelectMode = true;
        this.hasDefaultSelect = currentMode == ISelect.SINGLE_MODE;
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    public void longTouchSelectModeEnable(boolean longTouchSelectModeEnable) {
        longTouchEnable = longTouchSelectModeEnable;
    }

    public void setSelectedMode(@SelectMode int mode) {
        if (currentMode != mode) {
            currentMode = mode;
            isSelectMode = false;
            updateSelectMode(true);
        } else {
            if (!isSelectMode) {
                updateSelectMode(true);
            }
        }
    }

    void handleHolderClick(@NonNull final PowerHolder<T> holder) {
        if (holder.enableCLick) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPos = holder.getAdapterPosition();
                    if (currentPos == -1 || currentPos >= list.size()) {
                        return;
                    }
                    performClick(v, currentPos, getItem(currentPos));
                }
            });

            if (longTouchEnable) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int currentPos = holder.getAdapterPosition();
                        return !(currentPos == -1 || currentPos >= list.size()) && performLongClick(v,
                            holder.getAdapterPosition(), getItem(currentPos));
                    }
                });
            }
        }
    }

    @Override
    public void performClick(@NonNull final View itemView, final int position, T iSelect) {
        if (isSelectMode) {
            if (currentMode == ISelect.SINGLE_MODE && iSelect.isSelected() && hasDefaultSelect) {
                return;
            }
            toggleSelect(iSelect);
            if (currentMode == ISelect.SINGLE_MODE && prePos != -1 && position != prePos && iSelect.isSelected()) {
                T t1 = list.get(prePos);
                unSelect(t1);
                dispatchSelected(itemView, prePos, t1);
                notifyItemChanged(prePos, PAYLOAD_REFRESH_SELECT);
            }
            dispatchSelected(itemView, position, iSelect);
            notifyItemChanged(position, PAYLOAD_REFRESH_SELECT);
            prePos = position;
        } else {
            if (clickListener != null) {
                clickListener.onItemClick(itemView, position, iSelect);
            }
        }
    }

    private void dispatchSelected(View itemView, int position, T iSelect) {
        if (selectedListener != null) {
            selectedListener.onItemSelected(itemView, position, iSelect.isSelected());
            if (selectedBeans.isEmpty()) {
                selectedListener.onNothingSelected();
            }
        }
    }

    @Override
    public boolean performLongClick(@NonNull View itemView, int position, T iSelect) {
        if (longTouchEnable) {
            updateSelectMode(true);
            toggleSelect(iSelect);
            dispatchSelected(itemView, position, iSelect);
            notifyItemChanged(position, PAYLOAD_REFRESH_SELECT);
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
        if (list.isEmpty()) {
            return;
        }
        if (currentMode == ISelect.SINGLE_MODE) {
            return;
        }
        for (T bean : list) {
            select(bean);
        }
        notifyDataSetChanged();
    }

    public void resetAll() {
        if (list.isEmpty()) {
            return;
        }
        for (T iSelect : list) {
            unSelect(iSelect);
        }
        notifyDataSetChanged();
    }

    private void select(T item) {
        if (!selectedBeans.contains(item)) {
            item.setSelected(selectedBeans.add(item));
        }
    }

    private void unSelect(T item) {
        if (selectedBeans.contains(item)) {
            item.setSelected(!selectedBeans.remove(item));
        }
    }

    private void toggleSelect(T item) {
        boolean selected = item.isSelected();
        if (selected) {
            unSelect(item);
        } else {
            select(item);
        }
    }
}
