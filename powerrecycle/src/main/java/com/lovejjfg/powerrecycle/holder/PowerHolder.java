package com.lovejjfg.powerrecycle.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Joe on 2017/5/31.
 * Email lovejjfg@gmail.com
 */

public  class PowerHolder<T> extends RecyclerView.ViewHolder {
    public PowerHolder(View itemView) {
        super(itemView);
    }
    public  void onBind(T t){}

    public Context getContext() {
        return itemView.getContext();
    }

}

