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

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
public class NewBottomViewHolder<T> extends PowerHolder<T> {
    private final LinearLayout container;
    private final ProgressBar pb;
    private final TextView content;
    private View.OnClickListener listener;

    public NewBottomViewHolder(View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.footer_container);
        pb = itemView.findViewById(R.id.progressbar);
        content = itemView.findViewById(R.id.content);
    }

    public void onBind(final OnLoadMoreListener loadMoreListener, @LoadState int loadState) {
        switch (loadState) {
            case AdapterLoader.STATE_LASTED:
                pb.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                container.setOnClickListener(null);
                content.setText(R.string.text_no_more);

                break;
            case AdapterLoader.STATE_LOADING:
                content.setVisibility(View.VISIBLE);
                content.setText(R.string.text_load_more);
                container.setOnClickListener(null);
                pb.setVisibility(View.VISIBLE);
                if (loadMoreListener != null) {
                    loadMoreListener.onLoadMore();
                }
                break;
            case AdapterLoader.STATE_ERROR:
                container.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                content.setText(R.string.text_load_error);
                if (listener == null) {
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            content.setText(R.string.text_load_more);
                            pb.setVisibility(View.VISIBLE);
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
