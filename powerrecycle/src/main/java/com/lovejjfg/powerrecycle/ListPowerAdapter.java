/*
 * Copyright (c) 2018.  Joe
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
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import java.util.List;

@SuppressWarnings({ "unused", "unchecked", "WeakerAccess" })
public abstract class ListPowerAdapter<T> extends RecyclerView.Adapter<PowerHolder<T>> implements IAdapter<T> {
    public List<T> list;
    private final AsyncListDiffer<T> mHelper;
    @Nullable
    private OnItemLongClickListener<T> longClickListener;
    @Nullable
    OnItemClickListener<T> clickListener;

    protected RecyclerView recyclerView;

    protected ListPowerAdapter() {
        DiffUtil.ItemCallback<T> itemCallback = new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(T oldItem, T newItem) {
                return ListPowerAdapter.this.areItemsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return ListPowerAdapter.this.areContentsTheSame(oldItem, newItem);
            }

            @Override
            public Object getChangePayload(T oldItem, T newItem) {
                return ListPowerAdapter.this.getChangePayload(oldItem, newItem);
            }
        };
        mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this),
            new AsyncDifferConfig.Builder<>(itemCallback).build());
    }

    @Nullable
    @Override
    public View createEmptyView(@NonNull ViewGroup parent) {
        return null;
    }

    @Nullable
    @Override
    public View createErrorView(@NonNull ViewGroup parent) {
        return null;
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError(boolean force) {

    }

    @Override
    public void onErrorHolderBind(@NonNull PowerHolder<T> holder) {

    }

    @Override
    public void onEmptyHolderBind(@NonNull PowerHolder<T> holder) {

    }

    @SuppressWarnings("WeakerAccess")
    public void setList(@NonNull List<T> list) {
        mHelper.submitList(list);
        this.list = list;
    }

    @Override
    public void clearList() {

    }

    @SuppressWarnings("unused")
    @NonNull
    public T getItem(int position) {
        return mHelper.getCurrentList().get(position);
    }

    @Override
    public int getItemViewTypes(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mHelper.getCurrentList().size();
    }

    public abstract boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem);

    public abstract boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem);

    @Nullable
    public Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {
        return null;
    }

    @NonNull
    @Override
    public final PowerHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onViewHolderCreate(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(@NonNull PowerHolder<T> holder, int position) {
        bindDefaultHolder(holder, position, null);
    }

    @Override
    public final void onBindViewHolder(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads) {
        bindDefaultHolder(holder, position, payloads);
    }

    @Override
    public int getItemRealCount() {
        return 0;
    }

    private void bindDefaultHolder(@NonNull final PowerHolder<T> holder, int position,
        @Nullable List<Object> payloads) {
        handleHolderClick(holder);
        if (payloads == null || payloads.isEmpty()) {
            onViewHolderBind(holder, position);
        } else {
            onViewHolderBind(holder, position, payloads);
        }
    }

    @Override
    public void onViewHolderBind(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads) {
        onViewHolderBind(holder, position);
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
    public void attachRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
        //recyclerView.addOnScrollListener(new LoadMoreScrollListener(recyclerView));
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
    public int findFirstPositionOfType(int viewType) {
        return findFirstPositionOfType(viewType, 0);
    }

    @Override
    public int findFirstPositionOfType(int viewType, int offsetPosition) {
        if (list.isEmpty()) {
            return RecyclerView.NO_POSITION;
        }
        if (offsetPosition < 0 || offsetPosition > list.size() - 1) {
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
        if (list.isEmpty()) {
            return RecyclerView.NO_POSITION;
        }
        if (offsetPosition < 0 || offsetPosition > list.size() - 1) {
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
    public void setOnItemClickListener(@Nullable OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(@Nullable OnItemLongClickListener<T> listener) {
        this.longClickListener = listener;
    }

    @Override
    public void setErrorClickListener(@Nullable OnErrorClickListener errorClickListener) {

    }

    void handleHolderClick(@NonNull final PowerHolder<T> holder) {
        if (clickListener != null && holder.enableCLick) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPos = holder.getAdapterPosition();
                    if (currentPos < 0 || currentPos >= mHelper.getCurrentList().size()) {
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
                    return !(currentPos < 0 || currentPos >= mHelper.getCurrentList().size()) && performLongClick(
                        holder, holder.getAdapterPosition(), getItem(currentPos));
                }
            });
        }
    }
}
