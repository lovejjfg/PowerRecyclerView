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

import android.support.annotation.Nullable;
import android.view.View;
import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.OnLoadMoreListener;
import com.lovejjfg.powerrecycle.holder.AbsBottomViewHolder;

/**
 * Created by Joe on 2016/12/5.
 * Email lovejjfg@gmail.com
 */

public class BottomViewHolder extends AbsBottomViewHolder {

    private final View bottomView;

    public BottomViewHolder(View itemView) {
        super(itemView);
        bottomView = itemView.findViewById(R.id.progressbar);
    }

    @Override
    public void onBind(@Nullable OnLoadMoreListener loadMoreListener, int loadState) {
        if (loadState == AdapterLoader.STATE_LOADING) {
            itemView.getLayoutParams().height = 100;
            System.out.println("啦啦阿拉显示加载更多");
            //bottomView.setVisibility(View.VISIBLE);
            if (loadMoreListener != null) {
                loadMoreListener.onLoadMore();
            }
        } else {
            System.out.println("啦啦阿拉隐藏加载更多");
            //bottomView.setVisibility(View.GONE);
            itemView.getLayoutParams().height = 0;
        }
    }
}
