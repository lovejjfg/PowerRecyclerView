package com.lovejjfg.powerrecyclerx.manager;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by joe on 2017/11/30.
 * Email: lovejjfg@gmail.com
 */

// this fixed LayoutManger is used with notifyItemRemoved
@SuppressWarnings("unused")
public class FixedLinearLayoutManager extends LinearLayoutManager {
    public FixedLinearLayoutManager(Context context) {
        super(context);
    }

    public FixedLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FixedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
