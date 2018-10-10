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
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Joe on 2016/11/17.
 * Email lovejjfg@gmail.com
 */

public final class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private final int spacing;
    private final boolean showEdge;
    private final float pre;
    private final boolean showTopBottom;
    private int topSpace;
    private int bottomSpace;
    private final ItemOffsetsCallback callback;

    SpacesItemDecoration(int spacing, int spanCount, boolean showEdge, boolean showTopBottom,
        int topSpace, int bottomSpace, ItemOffsetsCallback callback) {
        this.spacing = spacing;
        this.spanCount = spanCount;
        this.showEdge = showEdge;
        this.showTopBottom = showTopBottom;
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
        this.callback = callback;
        this.pre = spacing * 1.0f / spanCount;
        if (topSpace == 0) {
            this.topSpace = spacing;
        }
        if (bottomSpace == 0) {
            this.bottomSpace = spacing;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
        RecyclerView parent, RecyclerView.State state) {
        if (callback != null && callback.callback(outRect, view, parent, state)) {
            return;
        }
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        int viewType = parent.getAdapter().getItemViewType(position);
        if (viewType == AdapterLoader.TYPE_BOTTOM
            || viewType == AdapterLoader.TYPE_EMPTY
            || viewType == AdapterLoader.TYPE_ERROR) {
            return;
        }
        int column = position % spanCount;
        if (showEdge) {
            outRect.left = (int) (spacing - column * pre);
            outRect.right = (int) ((column + 1) * pre);
        } else {
            outRect.left = (int) (column * pre);
            outRect.right = (int) (spacing - (column + 1) * pre);
        }

        if (showTopBottom) {
            if (position < spanCount) {
                outRect.top = topSpace;
            }
            outRect.bottom = bottomSpace;
        }
    }

    public static class Builder {
        int spacing;
        int spanCount;
        boolean showEdge;
        boolean showTopBottom;
        int topSpace;
        int bottomSpace;
        ItemOffsetsCallback callback;

        public Builder(int spacing, int spanCount, boolean showEdge) {
            this.spacing = spacing;
            this.spanCount = spanCount;
            this.showEdge = showEdge;
        }

        public Builder setSpacing(int spacing) {
            this.spacing = spacing;
            return this;
        }

        public Builder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        public Builder setShowEdge(boolean showEdge) {
            this.showEdge = showEdge;
            return this;
        }

        public Builder setShowTopBottom(boolean showTopBottom) {
            this.showTopBottom = showTopBottom;
            return this;
        }

        public Builder setTopSpace(int topSpace) {
            this.topSpace = topSpace;
            return this;
        }

        public Builder setBottomSpace(int bottomSpace) {
            this.bottomSpace = bottomSpace;
            return this;
        }

        public Builder setCallback(ItemOffsetsCallback callback) {
            this.callback = callback;
            return this;
        }

        public SpacesItemDecoration create() {
            if (spacing == 0 || spanCount == 0) {
                throw new IllegalArgumentException("You must set spanCount and spacing at first");
            }
            return new SpacesItemDecoration(spacing, spanCount, showEdge, showTopBottom, topSpace, bottomSpace,
                callback);
        }
    }

    public interface ItemOffsetsCallback {
        /**
         * @param outRect Rect to receive the output.
         * @param view The child view to decorate
         * @param parent RecyclerView this ItemDecoration is decorating
         * @param state The current state of RecyclerView.
         * @return True there was an assigned outRect that was handled, false
         * otherwise is returned.
         */
        boolean callback(Rect outRect, View view, RecyclerView parent, RecyclerView.State state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
