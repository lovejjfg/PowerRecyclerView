/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import java.util.Collections;
import java.util.List;

public class ModifyAsyncListDiffer<T> {
    private final ListUpdateCallback mUpdateCallback;
    private final ModifyAsyncDifferConfig<T> mConfig;
    private DataPreparedCallback dataPreparedCallback;

    public ModifyAsyncListDiffer(@NonNull ListUpdateCallback listUpdateCallback,
        @NonNull ModifyAsyncDifferConfig<T> config, DataPreparedCallback dataPreparedCallback) {
        mUpdateCallback = listUpdateCallback;
        mConfig = config;
        this.dataPreparedCallback = dataPreparedCallback;
    }

    @Nullable
    private List<T> mList;

    // Max generation of currently scheduled runnable
    private int mMaxScheduledGeneration;

    @NonNull
    public List<T> getCurrentList() {
        return mList == null ? Collections.<T>emptyList() : mList;
    }

    public void submitList(final List<T> newList) {
        if (newList != null && newList.equals(mList)) {
            return;
        }
        // incrementing generation means any currently-running diffs are discarded when they finish
        final int runGeneration = ++mMaxScheduledGeneration;

        // fast simple remove all
        if (newList == null) {
            //noinspection ConstantConditions
            int countRemoved = mList.size();
            mList = null;
            // notify last, after list is updated
            mUpdateCallback.onRemoved(0, countRemoved);
            return;
        }

        // fast simple first insert
        if (mList == null || mList.isEmpty()) {
            mList = newList;
            // notify last, after list is updated
            mUpdateCallback.onInserted(0, newList.size());
            return;
        }

        final List<T> oldList = mList;
        mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return oldList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newList.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return mConfig.getDiffCallback().areItemsTheSame(
                            oldList.get(oldItemPosition), newList.get(newItemPosition));
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return mConfig.getDiffCallback().areContentsTheSame(
                            oldList.get(oldItemPosition), newList.get(newItemPosition));
                    }

                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        return mConfig.getDiffCallback().getChangePayload(
                            oldList.get(oldItemPosition), newList.get(newItemPosition));
                    }
                });

                mConfig.getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mMaxScheduledGeneration == runGeneration) {
                            latchList(newList, result);
                        }
                    }
                });
            }
        });
    }

    private void latchList(@NonNull List<T> newList, @NonNull DiffUtil.DiffResult diffResult) {
        mList = newList;
        dataPreparedCallback.onDataPrepared();
        // notify last, after list is updated
        diffResult.dispatchUpdatesTo(mUpdateCallback);
    }

    interface DataPreparedCallback {
        void onDataPrepared();
    }
}
