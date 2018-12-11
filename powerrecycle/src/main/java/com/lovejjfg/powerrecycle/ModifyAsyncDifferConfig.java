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

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class ModifyAsyncDifferConfig<T> {
    @NonNull
    private final Executor mMainThreadExecutor;
    @NonNull
    private final Executor mBackgroundThreadExecutor;
    @NonNull
    private final DiffUtil.ItemCallback<T> mDiffCallback;

    protected ModifyAsyncDifferConfig(
        @NonNull Executor mainThreadExecutor,
        @NonNull Executor backgroundThreadExecutor,
        @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        mMainThreadExecutor = mainThreadExecutor;
        mBackgroundThreadExecutor = backgroundThreadExecutor;
        mDiffCallback = diffCallback;
    }

    @NonNull
    public Executor getMainThreadExecutor() {
        return mMainThreadExecutor;
    }

    @NonNull
    public Executor getBackgroundThreadExecutor() {
        return mBackgroundThreadExecutor;
    }

    @NonNull
    public DiffUtil.ItemCallback<T> getDiffCallback() {
        return mDiffCallback;
    }

    /**
     * Builder class for {@link ModifyAsyncDifferConfig}.
     */
    public static final class Builder<T> {
        private Executor mMainThreadExecutor;
        private Executor mBackgroundThreadExecutor;
        private final DiffUtil.ItemCallback<T> mDiffCallback;
        private static final Object EXECUTOR_LOCK = new Object();
        private static Executor DIFF_EXECUTOR = null;
        private static final Executor MAIN_THREAD_EXECUTOR = new MainThreadExecutor();

        Builder(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
            mDiffCallback = diffCallback;
        }

        private static class MainThreadExecutor implements Executor {
            final Handler mHandler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(@NonNull Runnable command) {
                mHandler.post(command);
            }
        }

        @NonNull
        public ModifyAsyncDifferConfig<T> build() {
            if (mMainThreadExecutor == null) {
                mMainThreadExecutor = MAIN_THREAD_EXECUTOR;
            }
            if (mBackgroundThreadExecutor == null) {
                synchronized (EXECUTOR_LOCK) {
                    if (DIFF_EXECUTOR == null) {
                        DIFF_EXECUTOR = Executors.newFixedThreadPool(2);
                    }
                }
                mBackgroundThreadExecutor = DIFF_EXECUTOR;
            }
            return new ModifyAsyncDifferConfig<>(
                mMainThreadExecutor,
                mBackgroundThreadExecutor,
                mDiffCallback);
        }
    }
}
