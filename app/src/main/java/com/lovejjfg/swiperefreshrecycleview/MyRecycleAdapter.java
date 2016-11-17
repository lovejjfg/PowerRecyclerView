package com.lovejjfg.swiperefreshrecycleview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lovejjfg.powerrecycle.OnItemClickListener;
import com.lovejjfg.powerrecycle.RefreshRecycleAdapter;
import com.lovejjfg.powerrecycle.SelectRefreshRecycleAdapter;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Joe on 2016-07-27
 * Email: lovejjfg@163.com
 */
public class MyRecycleAdapter extends SelectRefreshRecycleAdapter<TestBean> {



    @Override
    public RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onViewHolderBind(final RecyclerView.ViewHolder holder, final int position) {
        final TestBean testBean = list.get(position);
        ((MyViewHolder) holder).bindDateView(testBean);
        Log.e("TAG", "onViewHolderBind: " + position + "是否选中" + testBean.isSelected());
    }



    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv;
        private final CheckBox mCheckBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.text);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb);
        }

        public void bindDateView(TestBean s) {
            TransitionManager.beginDelayedTransition((ViewGroup) itemView, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_KEEP));
            mCheckBox.setVisibility(isSelectMode? View.VISIBLE : View.GONE);
            mTv.setText(s.isSelected() ? "选中：" + s.getName() : s.getName());
            mCheckBox.setChecked(s.isSelected());
        }
    }
}
