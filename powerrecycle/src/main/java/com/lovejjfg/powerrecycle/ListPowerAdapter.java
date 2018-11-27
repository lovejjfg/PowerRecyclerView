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
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import java.util.List;

@SuppressWarnings({ "unused", "unchecked", "WeakerAccess" })
public abstract class ListPowerAdapter<T> extends RecyclerView.Adapter<PowerHolder<T>> {

    private final AsyncListDiffer<T> mHelper;

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

    /**
     * Submits a new list to be diffed, and displayed.
     * <p>
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
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

    /**
     * Called to check whether two objects represent the same item.
     * <p>
     * For example, if your items have unique ids, this method should check their id equality.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the two items represent the same object or false if they are different.
     * @see DiffUtil.Callback#areItemsTheSame(int, int)
     */
    public abstract boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem);

    /**
     * When {@link #areItemsTheSame(T, T)} returns {@code true} for two items and
     * {@link #areContentsTheSame(T, T)} returns false for them, this method is called to
     * get a payload about the change.
     * <p>
     * For example, if you are using DiffUtil with {@link RecyclerView}, you can return the
     * particular field that changed in the item and your
     * {@link android.support.v7.widget.RecyclerView.ItemAnimator ItemAnimator} can use that
     * information to run the correct animation.
     * <p>
     * Default implementation returns {@code null}.
     *
     * @see DiffUtil.Callback#getChangePayload(int, int)
     */
    public abstract boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem);

    /**
     * When {@link #areItemsTheSame(T, T)} returns {@code true} for two items and
     * {@link #areContentsTheSame(T, T)} returns false for them, this method is called to
     * get a payload about the change.
     * <p>
     * For example, if you are using DiffUtil with {@link RecyclerView}, you can return the
     * particular field that changed in the item and your
     * {@link android.support.v7.widget.RecyclerView.ItemAnimator ItemAnimator} can use that
     * information to run the correct animation.
     * <p>
     * Default implementation returns {@code null}.
     *
     * @see DiffUtil.Callback#getChangePayload(int, int)
     */
    @Nullable
    public Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {
        return null;
    }
}
