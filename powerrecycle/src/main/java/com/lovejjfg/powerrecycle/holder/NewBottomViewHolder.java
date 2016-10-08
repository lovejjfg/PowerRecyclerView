package com.lovejjfg.powerrecycle.holder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.R;
import com.lovejjfg.powerrecycle.SwipeRefreshRecycleView;


/**
 * Created by Joe on 2016-03-11.
 * Email lovejjfg@gmail.com
 */
public class NewBottomViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout contaier;
    private ProgressBar pb;
    private TextView content;

    public NewBottomViewHolder(View itemView) {

        super(itemView);
        contaier = (LinearLayout) itemView.findViewById(R.id.footer_container);
        pb = (ProgressBar) itemView.findViewById(R.id.progressbar);
        content = (TextView) itemView.findViewById(R.id.content);
    }

    public void bindDateView(int state) {
        switch (state) {
            case AdapterLoader.STATE_LASTED:
                contaier.setVisibility(View.VISIBLE);
                contaier.setOnClickListener(null);
                pb.setVisibility(View.GONE);
                content.setText("---  没有更多了  ---");
                break;
            case AdapterLoader.STATE_LOADING:
                contaier.setVisibility(View.VISIBLE);
                content.setText("加载更多！！");
                contaier.setOnClickListener(null);
                pb.setVisibility(View.VISIBLE);
                break;
            case AdapterLoader.STATE_ERROR:
                contaier.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                content.setText("--- 加载更多失败点击重试 ---");
                contaier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        content.setText("加载更多！！");
                        pb.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

}
