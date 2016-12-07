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

package com.lovejjfg.swiperefreshrecycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.PowerRecyclerView;



/**
 * Created by Joe on 2016/12/5.
 * Email lovejjfg@gmail.com
 */

public class BottomViewHolder extends RecyclerView.ViewHolder {

    private final View bottomView;

    public BottomViewHolder(View itemView) {
        super(itemView);
        bottomView = itemView.findViewById(R.id.progressbar);
    }

    public void onBind(PowerRecyclerView.OnLoadMoreListener loadMoreListener, int loadState) {
        if (loadState == AdapterLoader.STATE_LOADING) {
            // TODO: 2016/12/5 handle error state
            bottomView.setVisibility(View.VISIBLE);
            loadMoreListener.onLoadMore();
        } else {
            bottomView.setVisibility(View.GONE);
        }
    }
}
