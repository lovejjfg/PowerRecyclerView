package com.lovejjfg.powerrecycle.holder;

import android.view.View;
import com.lovejjfg.powerrecycle.OnLoadMoreListener;
import com.lovejjfg.powerrecycle.annotation.LoadState;

/**
 * Created by joe on 2018/9/27.
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbsBottomViewHolder extends PowerHolder {

    public AbsBottomViewHolder(View itemView) {
        super(itemView, false);
    }

    public abstract void onBind(OnLoadMoreListener loadMoreListener, @LoadState int loadState);
}
