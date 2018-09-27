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

package com.lovejjfg.swiperefreshrecycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import com.lovejjfg.powerrecycle.SelectPowerAdapter;
import com.lovejjfg.powerrecycle.holder.AbsBottomViewHolder;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import com.lovejjfg.powerrecycle.model.ISelect;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;

/**
 * Created by Joe on 2016-07-27
 * Email: lovejjfg@163.com
 */
public class SelectRecycleAdapter extends SelectPowerAdapter<TestBean> {

    public SelectRecycleAdapter() {
        super(ISelect.SINGLE_MODE, false);
    }

    public SelectRecycleAdapter(int currentMode, boolean longTouchEnable) {
        super(currentMode, longTouchEnable);
    }

    @Override
    public PowerHolder<TestBean> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_select, parent, false);
        return new MyViewHolder<>(view);
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<TestBean> holder, final int position) {
        final TestBean testBean = list.get(position);
        ((MyViewHolder) holder).bindDateView(testBean);
        Log.e("TAG", "onViewHolderBind: " + position + "是否选中" + testBean.isSelected());
    }

    @Override
    public int getItemViewTypes(int position) {
        return super.getItemViewTypes(position);
    }

    private static class MyViewHolder<T> extends PowerHolder<T> {
        private final CheckedTextView mTv;

        MyViewHolder(View itemView) {
            super(itemView);
            mTv = (CheckedTextView) itemView.findViewById(R.id.text);
        }

        void bindDateView(TestBean s) {
            mTv.setText(s.isSelected() ? "选中：" + s.getName() : s.getName());
            mTv.setChecked(s.isSelected());
        }
    }

    @Override
    public AbsBottomViewHolder onBottomViewHolderCreate(@NonNull View loadMore) {
        return new BottomViewHolder(loadMore);
    }

    //you should Override this method to control whether the viewHolder can move and swipe or not! by default was
    // impossible!!
    @NonNull
    @Override
    public int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() % 2 == 0) {
            return new int[] { ItemTouchHelper.UP | ItemTouchHelper.DOWN
                | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_IDLE };
        }
        return super.getMovementFlags(recyclerView, viewHolder);
    }
}
