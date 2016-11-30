package com.lovejjfg.swiperefreshrecycleview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.lovejjfg.powerrecycle.SelectRefreshRecycleAdapter;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Joe on 2016-07-27
 * Email: lovejjfg@163.com
 */
public class SelectRecycleAdapter extends SelectRefreshRecycleAdapter<TestBean> {


    @Override
    public RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_select, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onViewHolderBind(final RecyclerView.ViewHolder holder, final int position) {
        final TestBean testBean = list.get(position);
        ((MyViewHolder) holder).bindDateView(testBean);
        Log.e("TAG", "onViewHolderBind: " + position + "是否选中" + testBean.isSelected());
    }


    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CheckedTextView mTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv = (CheckedTextView) itemView.findViewById(R.id.text);
        }

        public void bindDateView(TestBean s) {
            mTv.setText(s.isSelected() ? "选中：" + s.getName() : s.getName());
            mTv.setChecked(s.isSelected());
        }
    }
}
