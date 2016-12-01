package com.lovejjfg.powerrecycle.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.R;
import com.lovejjfg.powerrecycle.PowerRecyclerView;
import com.lovejjfg.powerrecycle.RefreshRecycleAdapter;


/**
 * Created by Joe on 2016-03-11.
 * Email lovejjfg@gmail.com
 */
public class NewBottomViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout container;
    private ProgressBar pb;
    private TextView content;
    private int height;

    public NewBottomViewHolder(View itemView) {

        super(itemView);
        container = (LinearLayout) itemView.findViewById(R.id.footer_container);
        pb = (ProgressBar) itemView.findViewById(R.id.progressbar);
        content = (TextView) itemView.findViewById(R.id.content);
    }

    public void bindDateView(RefreshRecycleAdapter adapter) {
        final PowerRecyclerView.OnRefreshLoadMoreListener loadMoreListener = adapter.getLoadMoreListener();
        switch (adapter.loadState) {
            case AdapterLoader.STATE_LASTED:
                pb.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                container.setOnClickListener(null);
                content.setText("---  没有更多了  ---");

                break;
            case AdapterLoader.STATE_LOADING:
                content.setVisibility(View.VISIBLE);
                content.setText("加载更多！！");
                container.setOnClickListener(null);
                pb.setVisibility(View.VISIBLE);
                if (loadMoreListener != null) {
                    loadMoreListener.onLoadMore();
                }
                break;
            case AdapterLoader.STATE_ERROR:
                container.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                content.setText("--- 加载更多失败点击重试 ---");
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (loadMoreListener != null) {
                            loadMoreListener.onLoadMore();
                        }
                        content.setText("加载更多！！");
                        pb.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

}
