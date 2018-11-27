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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import java.util.List;

@SuppressWarnings({ "unused", "unchecked", "WeakerAccess" })
public abstract class ListPowerAdapter<T> extends RecyclerView.Adapter<PowerHolder<T>> {

    private final AsyncListDiffer<T> mHelper;
    @Nullable
    private AdapterLoader.OnItemLongClickListener<T> longClickListener;
    @Nullable
    AdapterLoader.OnItemClickListener<T> clickListener;

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

    @SuppressWarnings("WeakerAccess")
    public void setList(List<T> list) {
        mHelper.submitList(list);
    }

    @SuppressWarnings("unused")
    @NonNull
    protected T getItem(int position) {
        return mHelper.getCurrentList().get(position);
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

    public abstract PowerHolder<T> onViewHolderCreate(@NonNull ViewGroup parent, int viewType);

    public abstract void onViewHolderBind(@NonNull PowerHolder<T> holder, int position);

    private void bindDefaultHolder(@NonNull final PowerHolder<T> holder, int position,
        @Nullable List<Object> payloads) {
        handleHolderClick(holder);
        if (payloads == null || payloads.isEmpty()) {
            onViewHolderBind(holder, position);
        } else {
            onViewHolderBind(holder, position, payloads);
        }
    }

    public void onViewHolderBind(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads) {
        onViewHolderBind(holder, position);
    }

    public void setOnItemClickListener(@NonNull AdapterLoader.OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(@Nullable AdapterLoader.OnItemLongClickListener<T> listener) {
        this.longClickListener = listener;
    }

    public void performClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item) {
        if (clickListener != null) {
            clickListener.onItemClick(holder, position, item);
        }
    }

    public boolean performLongClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item) {
        return longClickListener != null && longClickListener.onItemLongClick(holder, position, item);
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
