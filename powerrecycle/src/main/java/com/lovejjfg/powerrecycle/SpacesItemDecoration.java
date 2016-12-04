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

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by Joe on 2016/11/17.
 * Email lovejjfg@gmail.com
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;
    private final int spanCount;
    private final boolean showEdge;
    private final float pre;

    /**
     * @param space    item之间的空间
     * @param count    列数
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
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        int column = position % spanCount;
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

}
