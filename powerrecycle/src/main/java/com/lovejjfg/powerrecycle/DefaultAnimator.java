package com.lovejjfg.powerrecycle;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Joe on 2016-08-05
 * Email: lovejjfg@163.com
 */
public class DefaultAnimator extends DefaultItemAnimator {
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
        return true;
    }
}
