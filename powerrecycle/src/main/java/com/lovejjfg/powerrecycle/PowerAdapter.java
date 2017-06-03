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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovejjfg.powerrecycle.annotation.LoadState;
import com.lovejjfg.powerrecycle.holder.NewBottomViewHolder;
import com.lovejjfg.powerrecycle.holder.PowerHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joe on 2016-03-11
 * Email: lovejjfg@gmail.com
 */
public abstract class PowerAdapter<T> extends RecyclerView.Adapter<PowerHolder<T>> implements AdapterLoader<T>, SpanSizeCallBack, TouchHelperCallback.ItemDragSwipeCallBack {

    private View loadMore;
    private View errorView;
    private View emptyView;
    public int loadState;
    public boolean enableLoadMore;
    @Nullable
    private OnItemLongClickListener<T> longClickListener;
    @Nullable
    OnItemClickListener<T> clickListener;
    @Nullable
    OnItemSelectedListener selectedListener;
    private int currentType;

    public OnLoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private OnLoadMoreListener loadMoreListener;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        enableLoadMore = totalCount > list.size();
        notifyDataSetChanged();
    }

    private int totalCount;

    public PowerAdapter() {
        list = new ArrayList<>();
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public final void setList(List<T> data) {
        if (data == null) {
            return;
        }
        list.clear();
        appendList(data);
        enableLoadMore = totalCount > data.size();
    }

    public final void clearList() {
        list.clear();
    }

    @Override
    public final void appendList(List<T> data) {
        int positionStart = list.size();
        list.addAll(data);
        int itemCount = list.size() - positionStart;

        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionStart + 1, itemCount);
        }
    }

    @Override
    public T removeItem(int position) {
        if (position < 0 || position > list.size()) {
            return null;
        }
        T bean = list.remove(position);
        notifyItemRemoved(position);
        return bean;
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position > list.size()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public void insertItem(int position, T bean) {
        if (position < 0) {
            position = 0;
        }
        if (position > list.size()) {
            position = list.size();
        }
        list.add(position, bean);
        notifyItemInserted(position);
    }

    public final List<T> list;

    @Override
    public final PowerHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BOTTOM:
                if (loadMore != null) {
                    PowerHolder<T> holder = onBottomViewHolderCreate(loadMore);
                    if (holder == null) {
                        throw new RuntimeException("You must impl onBottomViewHolderCreate() and return your own holder ");
                    }
                    return holder;
                } else {
                    return new NewBottomViewHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_footer_new, parent, false));
                }
            case TYPE_ERROR:
                if (errorView == null) {
                    throw new NullPointerException("Did you forget init errorView?");
                }
                return new PowerHolder<T>(errorView);
            case TYPE_EMPT:
                if (emptyView == null) {
                    throw new NullPointerException("Did you forget init EmptyView?");
                }
                return new PowerHolder<T>(emptyView);
            default:
                return onViewHolderCreate(parent, viewType);
        }

    }


    @Override
    public PowerHolder<T> onBottomViewHolderCreate(View loadMore) {
        return null;
    }

    @Override
    public void onBottomViewHolderBind(RecyclerView.ViewHolder holder, @LoadState int loadState) {
        //todoNothing
    }

    @Override
    public abstract PowerHolder<T> onViewHolderCreate(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final PowerHolder<T> holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_BOTTOM:
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
                }
                loadState = loadState == STATE_ERROR ? STATE_ERROR : isHasMore() ? STATE_LOADING : STATE_LASTED;
                if (loadMore != null) {
                    try {
                        onBottomViewHolderBind(holder, loadState);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        ((NewBottomViewHolder) holder).bindDateView(this);
                    } catch (Exception e) {
//                    e.printStackTrace();
                    }
                }
                break;

            case TYPE_EMPT:
                onEmptyHolderBind(holder);
                break;
            case TYPE_ERROR:
                onErrorHolderBind(holder);
                break;
            default:
                if (clickListener != null) {
                    holder.itemView.setOnClickListener(v -> {
                        int position1 = holder.getAdapterPosition();
                        if (position1 == -1 || position1 >= list.size()) {
                            return;
                        }
                        performClick(v, position1, getItem(position));
                    });
                }
                if (longClickListener != null) {
                    holder.itemView.setOnLongClickListener(v -> {
                        int position12 = holder.getAdapterPosition();
                        return !(position12 == -1 || position12 >= list.size()) && performLongClick(v, holder.getAdapterPosition(), getItem(position12));
                    });
                }
                if (position == -1 || position >= list.size()) {
                    return;
                }
                onViewHolderBind(holder, position);
                break;
        }
    }

    protected void onErrorHolderBind(RecyclerView.ViewHolder holder) {

    }

    protected void onEmptyHolderBind(RecyclerView.ViewHolder holder) {

    }

    public void performClick(View itemView, int position, T item) {
        if (clickListener != null) {
            clickListener.onItemClick(itemView, position, item);
        }
    }

    @Override
    public boolean performLongClick(View itemView, int position, T item) {
        return longClickListener != null && longClickListener.onItemLongClick(itemView, position, item);
    }

    @Override
    public abstract void onViewHolderBind(RecyclerView.ViewHolder holder, int position);

    @Override
    public final void isLoadingMore() {
        if (loadState == STATE_LOADING) {
            return;
        }
        loadState = STATE_LOADING;
        notifyItemRangeChanged(getItemRealCount(), 1);
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? (currentType == 0) ? 0 : 1 : enableLoadMore ? list.size() + 1 : list.size();
    }

    @Override
    public int getItemRealCount() {
        return list.size();
    }

    @Override
    public final void setLoadMoreView(@NonNull View view) {
        loadMore = view;
    }

    @Override
    public final void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public final void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    @Override
    public void showEmpty() {
        list.clear();
        currentType = TYPE_EMPT;
        notifyDataSetChanged();
    }

    @Override
    public void showError(boolean force) {
        if (!force && !list.isEmpty()) {
            return;
        }
        list.clear();
        currentType = TYPE_ERROR;
        notifyDataSetChanged();
    }

    @Override
    public final int getItemViewType(int position) {
        if (list.isEmpty()) {
            return currentType;
        }
        if (!list.isEmpty() && position < list.size()) {
            return getItemViewTypes(position);
        } else {
            return TYPE_BOTTOM;
        }
    }

    @Override
    public int getItemViewTypes(int position) {
        return 0;
    }


    @Override
    public boolean isHasMore() {
        return getItemCount() < totalCount;
    }

    public final void loadMoreError() {
        loadState = STATE_ERROR;
        notifyItemRangeChanged(getItemRealCount(), 1);
    }

    @Override
    public void enableLoadMore(boolean loadMore) {
        if (enableLoadMore != loadMore) {
            enableLoadMore = loadMore;
            notifyDataSetChanged();
        }
    }

    @Override
    public void attachRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) {
            throw new NullPointerException("Did you forget call setLayoutManager() at first?");
        }
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return initSpanSize(position, (GridLayoutManager) manager);
                }
            });
        }
    }

    private int initSpanSize(int position, GridLayoutManager manager) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case AdapterLoader.TYPE_BOTTOM:
            case AdapterLoader.TYPE_EMPT:
            case AdapterLoader.TYPE_ERROR:
                return manager.getSpanCount();
            default:
                return getSpanSize(position);
        }
    }

    @Override
    public int getSpanSize(int position) {
        return 1;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.longClickListener = listener;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition == list.size() || toPosition == list.size()) {
            return false;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    void attachToRecyclerView(PowerRecyclerView recycleView) {
        longClickListener = recycleView.getLongClickListener();
        clickListener = recycleView.getClickListener();
        selectedListener = recycleView.getSelectedListener();
        loadMoreListener = recycleView.getLoadMoreClickListener();
    }

    @NonNull
    @Override
    public int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return new int[]{0, 0};
    }
}
