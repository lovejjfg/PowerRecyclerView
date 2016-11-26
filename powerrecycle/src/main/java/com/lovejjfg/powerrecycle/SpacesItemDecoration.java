package com.lovejjfg.powerrecycle;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Joe on 2016/11/17.
 * Email lovejjfg@gmail.com
 *
 */

/**
 * when use {@link android.support.v7.widget.GridLayoutManager},you can use this to add specified space.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private int spanCount;
    private boolean showEdge;
    private final float pre;

    /**
     *
     * @param space item之间的空间
     * @param count 列数
     * @param showEdge 是否显示左右边缘
     */
    public SpacesItemDecoration(int space, int count, boolean showEdge) {
        this.spacing = space;
        this.spanCount = count;
        this.showEdge = showEdge;
        pre = spacing * 1.0f / spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);
        int column = position % 3;
        if (showEdge) {
            outRect.left = (int) (spacing - column * pre);//left
            outRect.right = (int) ((column + 1) * pre);//right
        } else {
            outRect.left = (int) (column * pre);
            outRect.right = (int) (spacing - (column + 1) * pre);
        }

        if (position < spanCount) { // top
            outRect.top = spacing;
        }
        outRect.bottom = spacing; // bottom
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

}
