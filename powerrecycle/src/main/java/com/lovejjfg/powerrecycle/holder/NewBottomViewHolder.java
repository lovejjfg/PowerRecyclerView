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

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.OnLoadMoreListener;
import com.lovejjfg.powerrecycle.R;
import com.lovejjfg.powerrecycle.annotation.LoadState;

/**
 * Created by Joe on 2016-03-11.
 * Email lovejjfg@gmail.com
 */
@SuppressWarnings("Convert2Lambda")
public class NewBottomViewHolder extends AbsBottomViewHolder {
    private final View container;
    private final View loadingContainer;
    private final TextView content;
    private View.OnClickListener listener;

    public NewBottomViewHolder(View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.footer_container);
        loadingContainer = itemView.findViewById(R.id.footer_loading_container);
        content = itemView.findViewById(R.id.content);
    }

    @Override
    public void onBind(@Nullable final OnLoadMoreListener loadMoreListener, @LoadState int loadState) {
        switch (loadState) {
            case AdapterLoader.STATE_LASTED:
                loadingContainer.setVisibility(View.INVISIBLE);
                container.setOnClickListener(null);
                content.setVisibility(View.VISIBLE);
                content.setText(R.string.power_recycler_load_no_more);
                break;
            case AdapterLoader.STATE_LOADING:
                content.setVisibility(View.INVISIBLE);
                loadingContainer.setVisibility(View.VISIBLE);
                container.setOnClickListener(null);
                if (loadMoreListener != null) {
                    loadMoreListener.onLoadMore();
                }
                break;
            case AdapterLoader.STATE_ERROR:
                content.setVisibility(View.VISIBLE);
                content.setText(R.string.power_recycler_load_error);
                loadingContainer.setVisibility(View.INVISIBLE);
                if (listener == null) {
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            content.setVisibility(View.INVISIBLE);
                            loadingContainer.setVisibility(View.VISIBLE);
                            if (loadMoreListener != null) {
                                loadMoreListener.onLoadMore();
                            }
                        }
                    };
                }
                container.setOnClickListener(listener);
                break;
            default:
                break;
        }
    }
}
