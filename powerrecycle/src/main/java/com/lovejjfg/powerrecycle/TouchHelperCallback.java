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

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.lovejjfg.powerrecycle.holder.NewBottomViewHolder;



/**
 * Created by Joe on 2016-03-28
 * Email: lovejjfg@163.com
 */
public class TouchHelperCallback extends ItemTouchHelper.Callback {
    @SuppressWarnings("ConstantConditions")
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == AdapterLoader.TYPE_BOTTOM) {
            return makeMovementFlags(0, 0);
        }
        if (callBack != null) {
            int[] flags = callBack.getMovementFlags(recyclerView, viewHolder);
            if (flags == null || flags.length != 2) {
                throw new IllegalStateException("method getMovementFlags() should return type int[]  witch length mush be 2 ,by default you can call super.getMovementFlags().");
            }
            return makeMovementFlags(flags[0], flags[1]);
        }
        return makeMovementFlags(0, 0);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return callBack != null && callBack.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (callBack != null && !(viewHolder instanceof NewBottomViewHolder)) {
            callBack.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
                            boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                actionState, isCurrentlyActive);
    }


    public void setItemDragSwipeCallBack(@Nullable ItemDragSwipeCallBack callBack) {
        this.callBack = callBack;
    }

    @Nullable
    private ItemDragSwipeCallBack callBack;

    public interface ItemDragSwipeCallBack {
        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);

        /**
         * Instead of composing this flag manually, you can use makeMovementFlags(int, int) or makeFlag(int, int).
         * This flag is composed of 3 sets of 8 bits, where first 8 bits are for IDLE state, next 8 bits are for SWIPE state and third 8 bits are for DRAG state. Each 8 bit sections can be constructed by simply OR'ing direction flags defined in ItemTouchHelper.
         * For example, if you want it to allow swiping LEFT and RIGHT but only allow starting to swipe by swiping RIGHT, you can return:
         * <p>
         * <code>new  int[]{ItemTouchHelper.UP | ItemTouchHelper.DOWN |
         * ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_IDLE};</code>
         */
        @NonNull
        int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }


}
