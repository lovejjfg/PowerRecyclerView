package com.lovejjfg.powerrecycle.manager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by joe on 2017/11/30.
 * Email: lovejjfg@gmail.com
 */

// this fixed LayoutManger is used with notifyItemRemoved
public class FixedGrideLayoutManager extends GridLayoutManager {

    public FixedGrideLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FixedGrideLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FixedGrideLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
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
