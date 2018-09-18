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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.lovejjfg.powerrecycle.OnLoadMoreListener;
import com.lovejjfg.powerrecycle.SelectPowerAdapter;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import com.lovejjfg.powerrecycle.model.ISelect;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Joe on 2016-07-27
 * Email: lovejjfg@163.com
 */
public class SelectModeAdapter extends SelectPowerAdapter<TestBean> {
    public SelectModeAdapter() {
        super(ISelect.SINGLE_MODE, false);
    }

    public SelectModeAdapter(int currentMode, boolean longTouchEnable) {
        super(currentMode, longTouchEnable);
    }

    @Override
    public PowerHolder<TestBean> onViewHolderCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
        return new MyViewHolder<>(view);
    }

    @Override
    public void onViewHolderBind(PowerHolder<TestBean> holder, final int position) {
        final TestBean testBean = list.get(position);
        ((MyViewHolder) holder).bindDateView(isSelectMode, testBean);
        Log.e("TAG", "onViewHolderBind: " + position + "是否选中" + testBean.isSelected());
    }

    @Override
    public PowerHolder<TestBean> onBottomViewHolderCreate(View loadMore) {
        return new BottomViewHolder<>(loadMore);
    }

    @Override
    public void onBottomViewHolderBind(PowerHolder<TestBean> holder, OnLoadMoreListener listener, int loadState) {
        ((BottomViewHolder) holder).onBind(listener, loadState);
    }

    private static class MyViewHolder<T> extends PowerHolder<T> {

        private final TextView mTv;
        private final CheckBox mCheckBox;

        MyViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.text);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb);
        }

        void bindDateView(boolean isSelectMode, TestBean s) {
            TransitionManager.beginDelayedTransition((ViewGroup) itemView,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_KEEP));
            mCheckBox.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
            mTv.setText(s.isSelected() ? "选中：" + s.getName() : s.getName());
            mCheckBox.setChecked(s.isSelected());
        }
    }
}
