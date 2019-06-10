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

package com.lovejjfg.powerrecyclerx;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.lovejjfg.powerrecyclerx.model.ISelect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link SelectPowerAdapter} impl SelectMode,you can call  {@link #setSelectedMode(int)} to switch
 * {@link ISelect#SINGLE_MODE} or {@link ISelect#MULTIPLE_MODE}
 * and you can decide whether it's enableSelect the longTouch to jump to  SelectMode, you can call
 * {@link #longTouchSelectModeEnable(boolean)} to change ,by the way,the default was disable
 * <pre>
 *      // 1.set LayoutManager at first
 *     recycleView.layoutManager = GridLayoutManager(this, 2)
 *     adapter = CatsAdapter().apply {
 *         // 2. set adapter to recyclerView
 *         recycleView.adapter = this
 *         // allow to show loadmore item default is true
 *         this.enableLoadMore(false)
 *     }
 *     // 3. set ItemClickListener
 *     adapter.setOnItemClickListener { holder, position, item ->
 *        // perform item click
 *     }
 *     // 4. set LoadMoreListener
 *     adapter.setLoadMoreListener {
 *            // start to load more
 *     }
 *     // 5. SelectPowerAdapter setItemSelectListener
 *     adapter.setOnItemSelectListener(object : OnItemSelectedListener<Cat> {
 *         override fun onNothingSelected() {
 *         }
 *
 *         override fun onItemSelectChange(holder: PowerHolder<Cat>, position: Int, isSelected: Boolean) {
 *         }
 *     })
 *
 *     // create SelectPowerAdapter
 *     class CatsAdapter : SelectPowerAdapter<Cat>(ISelect.MULTIPLE_MODE, true) {
 *         override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<Cat> {
 *             return CatHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_cat, parent, false))
 *         }
 *
 *         override fun onViewHolderBind(holder: PowerHolder<Cat>, position: Int) {
 *             holder.onBind(list[position], isSelectMode)
 *         }
 *
 *         override fun onViewHolderBind(holder: PowerHolder<Cat>, position: Int, payloads: MutableList<Any>) {
 *             holder.onPartBind(list[position], isSelectMode, payloads)
 *         }
 *
 *         override fun getMaxSelectCount(): Int {
 *             return 3
 *         }
 *
 *         override fun onReceivedMaxSelectCount(count: Int) {
 *             Log.e("CatsAdapter", "onReceivedMaxSelectCount:$count")
 *         }
 *     }
 * </pre>
 */
@SuppressWarnings({ "unused" })
public abstract class SelectPowerAdapter<Select extends ISelect> extends PowerAdapter<Select> implements
    AdapterSelect<Select> {
    private int currentMode;
    private int prePos = RecyclerView.NO_POSITION;
    public boolean isCancelAble;
    private boolean longTouchEnable;
    protected boolean isSelectMode;
    @Nullable
    private OnItemSelectedListener<Select> selectedListener;
    private final ArrayList<Select> selectedList = new ArrayList<>();

    public SelectPowerAdapter(@SelectMode int currentMode, boolean longTouchEnable) {
        this.currentMode = currentMode;
        this.longTouchEnable = longTouchEnable;
        this.isSelectMode = true;
        this.isCancelAble = currentMode != ISelect.SINGLE_MODE;
    }

    @Override
    public void appendList(@NonNull List<Select> data) {
        super.appendList(data);
        for (Select select : data) {
            if (select.isSelected()) {
                select.setSelected(false);
                setCurrentPos(list.indexOf(select));
            }
        }
    }

    @NonNull
    @Override
    public Set<Select> getSelectedItems() {
        return new HashSet<>(selectedList);
    }

    @Override
    public void updateSelectMode(boolean isSelect) {
        if (isSelectMode != isSelect) {
            isSelectMode = isSelect;
            prePos = RecyclerView.NO_POSITION;
            this.isCancelAble = currentMode != ISelect.SINGLE_MODE;
            resetAll(true);
        }
    }

    @Override
    public void updateCancelAble(boolean isCancelAble) {
        this.isCancelAble = isCancelAble;
    }

    @Override
    public boolean isCancelAble() {
        return isCancelAble;
    }

    @Override
    public void setCurrentPos(int position) {
        if (checkIllegalPosition(position)) {
            return;
        }
        Select select = list.get(position);
        if (checkMaxCount(select)) {
            return;
        }
        handlePrePos(position);
        if (select(select)) {
            checkAndDispatchHolder(position, select);
            notifyItemChanged(position, PAYLOAD_REFRESH_SELECT);
        }
    }

    @Override
    public void setCurrentPositions(@NonNull int... positions) {
        if (positions.length == 0) {
            return;
        }
        for (int position : positions) {
            setCurrentPos(position);
        }
    }

    @Override
    public boolean isSelectMode() {
        return isSelectMode;
    }

    @Override
    public void longTouchSelectModeEnable(boolean longTouchSelectModeEnable) {
        longTouchEnable = longTouchSelectModeEnable;
    }

    @Override
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

    void handleHolderClick(@NonNull final PowerHolder<Select> holder) {
        if (holder.enableCLick) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPos = holder.getAdapterPosition();
                    if (checkIllegalPosition(currentPos)) {
                        return;
                    }
                    //noinspection ConstantConditions
                    performClick(holder, currentPos, getItem(currentPos));
                }
            });

            if (longTouchEnable) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int currentPos = holder.getAdapterPosition();
                        //noinspection ConstantConditions
                        return !checkIllegalPosition(currentPos)
                            && performLongClick(holder, currentPos, getItem(currentPos));
                    }
                });
            }
        }
    }

    @Override
    public void performClick(@NonNull final PowerHolder<Select> powerHolder, final int position,
        @NonNull Select select) {
        if (isSelectMode) {
            handleClick(powerHolder, position, select);
        } else {
            if (clickListener != null) {
                clickListener.onItemClick(powerHolder, position, select);
            }
        }
    }

    @Override
    public boolean performLongClick(@NonNull PowerHolder<Select> holder, int position, @NonNull Select select) {
        if (longTouchEnable) {
            updateSelectMode(true);
            handleClick(holder, position, select);
            return true;
        } else {
            return super.performLongClick(holder, position, select);
        }
    }

    @Override
    public void setOnItemSelectListener(OnItemSelectedListener<Select> listener) {
        this.selectedListener = listener;
    }

    @Override
    public void selectAll() {
        if (currentMode == ISelect.SINGLE_MODE) {
            return;
        }
        if (list.isEmpty()) {
            return;
        }
        int size = list.size();

        for (int i = 0; i < size; i++) {
            Select select = list.get(i);
            if (select(select)) {
                checkAndDispatchHolder(i, select);
                notifyItemChanged(i, PAYLOAD_REFRESH_SELECT);
            }
        }
    }

    @Override
    public void resetAll(boolean force) {
        if (!isCancelAble && !force) {
            return;
        }
        if (list.isEmpty()) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Select select = list.get(i);
            if (unSelect(select)) {
                checkAndDispatchHolder(i, select);
                if (!force) {
                    notifyItemChanged(i, PAYLOAD_REFRESH_SELECT);
                }
            }
        }
        if (force) {
            notifyItemRangeChanged(0, list.size(), PAYLOAD_REFRESH_SELECT);
        }
    }

    @Override
    public void resetAll() {
        resetAll(false);
    }

    @Override
    public void clearList(boolean notify) {
        prePos = RecyclerView.NO_POSITION;
        selectedList.clear();
        super.clearList(notify);
    }

    @Override
    public void clearSelectList(boolean notify) {
        if (!isCancelAble) {
            return;
        }
        if (selectedList.isEmpty()) {
            return;
        }
        int size = selectedList.size();
        for (int i = 0; i < size; i++) {
            Select item = selectedList.get(0);
            if (unSelect(item) && notify) {
                int index = list.indexOf(item);
                if (index < 0 || index > list.size() - 1) {
                    return;
                }
                checkAndDispatchHolder(index, item);
                notifyItemChanged(index, PAYLOAD_REFRESH_SELECT);
            }
        }
        if (!selectedList.isEmpty()) {
            selectedList.clear();
        }
    }

    @Override
    public void deleteSelectedItems() {
        if (!isCancelAble) {
            return;
        }
        if (selectedList.isEmpty()) {
            return;
        }
        int size = selectedList.size();
        for (int i = 0; i < size; i++) {
            Select item = selectedList.get(0);
            int position = list.indexOf(item);
            removeItem(position);
        }
        if (!selectedList.isEmpty()) {
            selectedList.clear();
        }
    }

    @Override
    public void revertAllSelected() {
        if (!isCancelAble) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Select select = list.get(i);
            if (toggleSelect(select)) {
                checkAndDispatchHolder(i, select);
                notifyItemChanged(i, PAYLOAD_REFRESH_SELECT);
            }
        }
    }

    @Override
    public int getMaxSelectCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onReceivedMaxSelectCount(int count) {
    }

    @Override
    public boolean updateItem(@NonNull Select item, @Nullable Object payload) {
        if (!isSelectMode) {
            return super.updateItem(item, payload);
        }
        int index = list.indexOf(item);
        if (checkIllegalPosition(index)) {
            return false;
        }
        if (!item.isSelected() && selectedList.contains(item)) {
            selectedList.remove(item);
            checkAndDispatchHolder(index, item);
            if (payload != null) {
                notifyItemChanged(index, payload);
                notifyItemChanged(index, PAYLOAD_REFRESH_SELECT);
            } else {
                notifyItemChanged(index, PAYLOAD_REFRESH_SELECT);
            }
        } else if (item.isSelected() && !selectedList.contains(item)) {
            item.setSelected(false);
            if (checkMaxCount(item)) {
                notifyItemChanged(index, payload);
                return false;
            }
            item.setSelected(true);
            selectedList.add(item);
            checkAndDispatchHolder(index, item);
            if (payload != null) {
                notifyItemChanged(index, payload);
                notifyItemChanged(index, PAYLOAD_REFRESH_SELECT);
            } else {
                notifyItemChanged(index, PAYLOAD_REFRESH_SELECT);
            }
        } else {
            notifyItemChanged(index, payload);
        }
        return true;
    }

    @Override
    public Select removeItem(int position) {
        if (!isSelectMode) {
            return super.removeItem(position);
        }
        if (checkIllegalPosition(position)) {
            return null;
        }
        Select select = list.remove(position);
        if (unSelect(select)) {
            checkAndDispatchHolder(position, select);
            notifyItemRemoved(position);
        }
        return select;
    }

    private void checkAndDispatchHolder(int position, Select select) {
        if (!isSelectMode) {
            return;
        }
        @SuppressWarnings("unchecked")
        PowerHolder<Select> h = getViewHolder(position);
        if (h != null) {
            dispatchSelected(h, position, select);
        }
    }

    @Nullable
    private PowerHolder getViewHolder(int index) {
        if (recyclerView == null) {
            throw new NullPointerException("Did you forget call attachRecyclerView() at first?");
        }
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(index);
        if (holder instanceof PowerHolder) {
            return (PowerHolder) holder;
        }
        return null;
    }

    private boolean select(Select item) {
        if (item == null) {
            return false;
        }
        if (item.isSelected() && selectedList.contains(item)) {
            return false;
        }
        item.setSelected(true);
        return selectedList.add(item);
    }

    private boolean unSelect(Select item) {
        if (item == null) {
            return false;
        }
        if (!item.isSelected() && !selectedList.contains(item)) {
            return false;
        }
        item.setSelected(false);
        return selectedList.remove(item);
    }

    private boolean toggleSelect(Select item) {
        if (item == null) {
            return false;
        }
        boolean selected = item.isSelected();
        if (selected) {
            return unSelect(item);
        } else {
            return select(item);
        }
    }

    private void dispatchSelected(PowerHolder<Select> holder, int position, Select select) {
        if (isSelectMode && selectedListener != null) {
            selectedListener.onItemSelectChange(holder, position, select.isSelected());
            if (selectedList.isEmpty()) {
                selectedListener.onNothingSelected();
            }
        }
    }

    private void handleClick(@NonNull PowerHolder<Select> powerHolder, int position, Select select) {
        if (select.isSelected() && !isCancelAble) {
            return;
        }
        if (checkMaxCount(select)) {
            return;
        }
        if (toggleSelect(select)) {
            handlePrePos(position);
            dispatchSelected(powerHolder, position, select);
            notifyItemChanged(position, PAYLOAD_REFRESH_SELECT);
        }
    }

    private boolean checkMaxCount(Select select) {
        if (currentMode == ISelect.MULTIPLE_MODE
            && !select.isSelected()
            && selectedList.size() >= getMaxSelectCount()) {
            onReceivedMaxSelectCount(selectedList.size());
            return true;
        }
        return false;
    }

    private void handlePrePos(int position) {
        if (currentMode == ISelect.SINGLE_MODE && position != prePos) {
            if (prePos >= 0 && prePos <= list.size() - 1) {
                Select select = list.get(prePos);
                if (unSelect(select)) {
                    checkAndDispatchHolder(prePos, select);
                    notifyItemChanged(prePos, PAYLOAD_REFRESH_SELECT);
                }
            }
            prePos = position;
        }
    }
}
