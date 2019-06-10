package com.lovejjfg.powerrecyclerx;

import android.view.View;
import androidx.annotation.NonNull;

/**
 * Created by joe on 2018/9/27.
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbsBottomViewHolder extends PowerHolder {

    public AbsBottomViewHolder(@NonNull View itemView) {
        super(itemView, false);
    }

    public abstract void onBind(@NonNull OnLoadMoreListener loadMoreListener, @LoadState int loadState);
}
