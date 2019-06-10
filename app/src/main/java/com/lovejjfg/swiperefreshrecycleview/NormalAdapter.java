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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lovejjfg.powerrecyclerx.PowerAdapter;
import com.lovejjfg.powerrecyclerx.PowerHolder;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;


/**
 * Created by Joe on 2016-07-27
 * Email: lovejjfg@163.com
 */
public class NormalAdapter extends PowerAdapter<TestBean> {


    @Override
    public PowerHolder<TestBean> onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<TestBean> holder, final int position) {
        final TestBean testBean = list.get(position);
        ((MyViewHolder) holder).bindDateView(testBean);
        Log.e("TAG", "onViewHolderBind: " + position + "是否选中" + testBean.isSelected());
    }

    @NonNull
    @Override
    public int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return new int[]{ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_IDLE};
    }

    private static class MyViewHolder extends PowerHolder<TestBean> {

        private final TextView mTv;
        private final CheckBox mCheckBox;

        MyViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.text);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb);
        }

        void bindDateView(TestBean s) {
            TransitionManager.beginDelayedTransition((ViewGroup) itemView, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_KEEP));
            mTv.setText(s.getName());
            mCheckBox.setVisibility(View.GONE);
        }
    }


}
