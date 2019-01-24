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

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lovejjfg.powerrecycle.annotation.LoadState;
import com.lovejjfg.powerrecycle.holder.AbsBottomViewHolder;
import com.lovejjfg.powerrecycle.holder.NewBottomViewHolder;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joe on 2016-03-11
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings({ "unused", "unchecked" })
public abstract class PowerAdapter<T> extends RecyclerView.Adapter<PowerHolder<T>> implements AdapterLoader<T>,
    SpanSizeCallBack, TouchHelperCallback.ItemDragSwipeCallBack {
    private static final String TAG = PowerAdapter.class.getSimpleName();
    public final List<T> list;
    public boolean enableLoadMore;
    private int totalCount;
    private int currentType;
    @LayoutRes
    private int loadMoreLayout = RecyclerView.INVALID_TYPE;
    @LoadState
    private int loadState;
    @Nullable
    private View errorView;
    @Nullable
    private View emptyView;
    @Nullable
    RecyclerView recyclerView;
    @Nullable
    private OnLoadMoreListener loadMoreListener;
    @Nullable
    private OnErrorClickListener errorClickListener;
    @Nullable
    private OnItemLongClickListener<T> longClickListener;
    @Nullable
    OnItemClickListener<T> clickListener;

    private Runnable loadMoreAction;

    private Runnable loadMoreErrorAction;

    public PowerAdapter() {
        list = new ArrayList<>();
    }

    @Nullable
    public OnLoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(@NonNull OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        enableLoadMore = totalCount > list.size();
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public final void setList(@NonNull List<T> data) {
        if (data.isEmpty()) {
            return;
        }
        clearList(false);
        appendList(data);
        enableLoadMore = totalCount > data.size();
    }

    @Override
    public final void insertList(@NonNull List<T> data, int startPos) {
        if (startPos < 0 || startPos > list.size()) {
            return;
        }
        list.addAll(startPos, data);
        notifyItemRangeInserted(startPos, data.size());
    }

    @Override
    public void clearList() {
        clearList(true);
    }

    @Override
    public void clearList(boolean notify) {
        list.clear();
        loadState = 0;
        currentType = 0;
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public final void appendList(@NonNull List<T> data) {
        if (data.isEmpty()) {
            return;
        }
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
        if (checkIllegalPosition(position)) {
            return null;
        }
        T bean = list.remove(position);
        notifyItemRemoved(position);
        return bean;
    }

    @Override
    public final boolean updateItem(@NonNull T item) {
        return updateItem(item, null);
    }

    @Override
    public boolean updateItem(@NonNull T item, @Nullable Object payload) {
        int index = list.indexOf(item);
        if (checkIllegalPosition(index)) {
            return false;
        }
        notifyItemChanged(index, payload);
        return true;
    }

    @Override
    public T getItem(int position) {
        return checkIllegalPosition(position) ? null : list.get(position);
    }

    @Override
    public void insertItem(int position, @NonNull T item) {
        if (position < 0) {
            position = 0;
        }
        if (position > list.size()) {
            position = list.size();
        }
        list.add(position, item);
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public final PowerHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BOTTOM:
                return createBottomHolder(parent);
            case TYPE_ERROR:
                return createErrorHolder(parent);
            case TYPE_EMPTY:
                return createEmptyHolder(parent);
            default:
                return onViewHolderCreate(parent, viewType);
        }
    }

    @NonNull
    private PowerHolder<T> createEmptyHolder(@NonNull ViewGroup parent) {
        if (emptyView == null) {
            emptyView = createEmptyView(parent);
            if (emptyView != null) {
                setEmptyView(emptyView);
            } else {
                throw new NullPointerException("Did you forget init EmptyView?");
            }
        }
        return new PowerHolder<>(emptyView);
    }

    @NonNull
    private PowerHolder<T> createErrorHolder(@NonNull ViewGroup parent) {
        if (errorView == null) {
            errorView = createErrorView(parent);
            if (errorView != null) {
                setErrorView(errorView);
            } else {
                throw new NullPointerException("Did you forget init ErrorView?");
            }
        }
        return new PowerHolder<>(errorView);
    }

    private PowerHolder<T> createBottomHolder(@NonNull ViewGroup parent) {
        if (loadMoreLayout != -1) {
            View view =
                LayoutInflater.from(parent.getContext()).inflate(loadMoreLayout, parent, false);
            AbsBottomViewHolder holder = onBottomViewHolderCreate(view);
            if (holder == null) {
                throw new RuntimeException(
                    "You must impl onBottomViewHolderCreate() and return your own holder ");
            }
            return holder;
        } else {
            View loadMoreView = createLoadMoreView(parent);
            if (loadMoreView != null) {
                return onBottomViewHolderCreate(loadMoreView);
            } else {
                //noinspection unchecked
                return new NewBottomViewHolder(
                    LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_footer_new, parent, false));
            }
        }
    }

    @Override
    public AbsBottomViewHolder onBottomViewHolderCreate(@NonNull View loadMore) {
        return null;
    }

    @Override
    public void onBottomViewHolderBind(@NonNull AbsBottomViewHolder holder, @Nullable OnLoadMoreListener listener,
        @LoadState int loadState) {
        holder.onBind(listener, loadState);
    }

    @Override
    public abstract PowerHolder<T> onViewHolderCreate(@NonNull ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(@NonNull final PowerHolder<T> holder, int position) {
        bindViewHolder(holder, position, null);
    }

    @Override
    public final void onBindViewHolder(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads) {
        bindViewHolder(holder, position, payloads);
    }

    private void bindViewHolder(@NonNull PowerHolder<T> holder, int position, List<Object> o) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_BOTTOM:
                bindBottom(holder);
                break;
            case TYPE_EMPTY:
                onEmptyHolderBind(holder);
                break;
            case TYPE_ERROR:
                onErrorHolderBind(holder);
                break;
            default:
                bindDefaultHolder(holder, position, o);
                break;
        }
    }

    private void bindDefaultHolder(@NonNull final PowerHolder<T> holder, int position,
        @Nullable List<Object> payloads) {
        if (checkIllegalPosition(position)) {
            return;
        }
        handleHolderClick(holder);
        if (payloads == null || payloads.isEmpty()) {
            onViewHolderBind(holder, position);
        } else {
            onViewHolderBind(holder, position, payloads);
        }
    }

    void handleHolderClick(@NonNull final PowerHolder<T> holder) {
        if (clickListener != null && holder.enableCLick) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPos = holder.getAdapterPosition();
                    if (checkIllegalPosition(currentPos)) {
                        return;
                    }
                    //noinspection ConstantConditions
                    performClick(holder, currentPos, getItem(currentPos));
                }
            });
        }
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int currentPos = holder.getAdapterPosition();
                    //noinspection ConstantConditions
                    return !checkIllegalPosition(currentPos) && performLongClick(
                        holder, currentPos, getItem(currentPos));
                }
            });
        }
    }

    private void bindBottom(@NonNull PowerHolder<T> holder) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
        loadState = loadState == STATE_ERROR ? STATE_ERROR : isHasMore() ? STATE_LOADING : STATE_LASTED;
        try {
            if (loadMoreLayout != -1) {
                onBottomViewHolderBind((AbsBottomViewHolder) holder, loadMoreListener, loadState);
            } else {
                ((NewBottomViewHolder) holder).onBind(loadMoreListener, loadState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View createErrorView(@NonNull ViewGroup parent) {
        return null;
    }

    @Nullable
    @Override
    public View createEmptyView(@NonNull ViewGroup parent) {
        return null;
    }

    @Nullable
    @Override
    public View createLoadMoreView(@NonNull ViewGroup parent) {
        return null;
    }

    @Override
    public void onErrorHolderBind(@NonNull PowerHolder<T> holder) {
    }

    @Override
    public void onEmptyHolderBind(@NonNull PowerHolder<T> holder) {
    }

    @Override
    public void performClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item) {
        if (clickListener != null) {
            clickListener.onItemClick(holder, position, item);
        }
    }

    @Override
    public boolean performLongClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item) {
        return longClickListener != null && longClickListener.onItemLongClick(holder, position, item);
    }

    @Override
    public abstract void onViewHolderBind(@NonNull PowerHolder<T> holder, int position);

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads) {
        onViewHolderBind(holder, position);
    }

    @Override
    public final void updateLoadingMore() {
        if (loadState == STATE_LOADING) {
            return;
        }
        //fix crash :https://github.com/lovejjfg/PowerRecyclerView/issues/2
        if (loadMoreAction == null) {
            loadMoreAction = new Runnable() {
                @Override
                public void run() {
                    loadState = STATE_LOADING;
                    notifyItemRangeChanged(getItemRealCount(), 1, PAYLOAD_REFRESH_BOTTOM);
                }
            };
        }
        if (recyclerView == null) {
            throw new NullPointerException("Did you forget call attachRecyclerView() at first?");
        }
        recyclerView.post(loadMoreAction);
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
    public final void setLoadMoreView(@LayoutRes int layoutRes) {
        loadMoreLayout = layoutRes;
    }

    @Override
    public final void setEmptyView(@NonNull View emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public final void setErrorView(@NonNull View errorView) {
        this.errorView = errorView;
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorClickListener != null) {
                    errorClickListener.onErrorClick(v);
                }
            }
        });
    }

    @Override
    public void showEmpty() {
        clearList(false);
        currentType = TYPE_EMPTY;
        notifyDataSetChanged();
    }

    public void showError() {
        showError(true);
    }

    @Override
    public void showError(boolean force) {
        if (!force && !list.isEmpty()) {
            return;
        }
        clearList(false);
        currentType = TYPE_ERROR;
        notifyDataSetChanged();
    }

    @Override
    public final int getItemViewType(int position) {
        if (list.isEmpty()) {
            if (currentType != 0) {
                return currentType;
            }
            return super.getItemViewType(position);
        }
        if (position < list.size()) {
            return getItemViewTypes(position);
        } else {
            return TYPE_BOTTOM;
        }
    }

    @Override
    public int findFirstPositionOfType(int viewType) {
        return findFirstPositionOfType(viewType, 0);
    }

    @Override
    public int findFirstPositionOfType(int viewType, int offsetPosition) {
        if (checkIllegalPosition(offsetPosition)) {
            return RecyclerView.NO_POSITION;
        }
        for (int i = offsetPosition; i < list.size(); i++) {
            if (getItemViewType(i) == viewType) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    @Override
    public int findLastPositionOfType(int viewType) {
        return findLastPositionOfType(viewType, list.size() - 1);
    }

    @Override
    public int findLastPositionOfType(int viewType, int offsetPosition) {
        if (checkIllegalPosition(offsetPosition)) {
            return RecyclerView.NO_POSITION;
        }
        for (int i = offsetPosition; i >= 0; i--) {
            if (getItemViewType(i) == viewType) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    @Override
    public int getItemViewTypes(int position) {
        return 0;
    }

    @Override
    public boolean isHasMore() {
        return getItemRealCount() < totalCount;
    }

    public final void loadMoreError() {
        if (loadState == STATE_ERROR) {
            return;
        }
        if (loadMoreErrorAction == null) {
            loadMoreErrorAction = new Runnable() {
                @Override
                public void run() {
                    loadState = STATE_ERROR;
                    notifyItemRangeChanged(getItemRealCount(), 1, PAYLOAD_REFRESH_BOTTOM);
                }
            };
        }
        if (recyclerView == null) {
            throw new NullPointerException("Did you forget call attachRecyclerView() at first?");
        }
        recyclerView.post(loadMoreErrorAction);
    }

    @Override
    public void enableLoadMore(boolean loadMore) {
        if (enableLoadMore != loadMore) {
            enableLoadMore = loadMore;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(recyclerView));
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
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

    @Override
    public final void attachRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
    }

    private int initSpanSize(int position, GridLayoutManager manager) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case AdapterLoader.TYPE_BOTTOM:
            case AdapterLoader.TYPE_EMPTY:
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

    @Override
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }

    @Override
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

    @NonNull
    @Override
    public int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return new int[] { 0, 0 };
    }

    @Nullable
    public OnErrorClickListener getErrorClickListener() {
        return errorClickListener;
    }

    @Override
    public void setErrorClickListener(@Nullable OnErrorClickListener errorClickListener) {
        this.errorClickListener = errorClickListener;
    }

    @Override
    public boolean checkIllegalPosition(int position) {
        return list.isEmpty() || position < 0 || position >= list.size();
    }
}
