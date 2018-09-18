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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Joe on 2016/12/6.
 * Email lovejjfg@gmail.com
 */

class LoadMoreScrollListener extends RecyclerView.OnScrollListener {
    private final RecyclerView.LayoutManager manager;
    private final PowerAdapter adapter;

    LoadMoreScrollListener(RecyclerView recyclerView) {
        manager = recyclerView.getLayoutManager();
        adapter = (PowerAdapter) recyclerView.getAdapter();
        if (null == manager) {
            throw new RuntimeException("You should call setLayoutManager() first!!");
        }
        if (null == adapter) {
            throw new RuntimeException("You should call setAdapter() first!!");
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (manager instanceof LinearLayoutManager) {
            int lastCompletelyVisibleItemPosition =
                ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();

            if (adapter.getItemCount() > 1 && lastCompletelyVisibleItemPosition >= adapter.getItemCount() - 1 && adapter
                .isHasMore()) {
                adapter.updateLoadingMore();
            }
        }
        if (manager instanceof StaggeredGridLayoutManager) {
            int count = ((StaggeredGridLayoutManager) manager).getSpanCount();
            int[] itemPositions = new int[count];
            ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(itemPositions);
            int lastVisibleItemPosition = itemPositions[0];
            for (int i = count - 1; i > 0; i--) {
                if (lastVisibleItemPosition < itemPositions[i]) {
                    lastVisibleItemPosition = itemPositions[i];
                }
            }
            if (lastVisibleItemPosition >= adapter.getItemCount() - 1 && adapter.isHasMore()) {
                adapter.updateLoadingMore();
            }
        }
    }
}
