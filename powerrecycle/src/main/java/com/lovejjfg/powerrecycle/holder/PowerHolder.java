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

package com.lovejjfg.powerrecycle.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.List;

/**
 * Created by Joe on 2017/5/31.
 * Email lovejjfg@gmail.com
 * <p> Impl ViewHolder ,you can get Context and finish DataBind with onBind</p>
 */

public class PowerHolder<T> extends RecyclerView.ViewHolder {
    public boolean enableCLick = true;

    public PowerHolder(@NonNull View itemView) {
        super(itemView);
    }

    public PowerHolder(@NonNull View itemView, boolean enableCLick) {
        super(itemView);
        this.enableCLick = enableCLick;
    }

    public void onBind(@NonNull T t) {
    }

    public void onBind(@NonNull T t, boolean isSelectMode) {

    }

    public void onPartBind(@NonNull T t, @NonNull List<Object> payloads) {

    }

    public void onPartBind(@NonNull T t, boolean isSelectMode, @NonNull List<Object> payloads) {

    }

    public @Nullable Context getContext() {
        return itemView.getContext();
    }
}
