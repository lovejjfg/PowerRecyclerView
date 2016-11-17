package com.lovejjfg.powerrecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lovejjfg.powerrecycle.holder.NewBottomViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 2016-03-11
 * Email: lovejjfg@gmail.com
 */
public abstract class RefreshRecycleAdapter<T> extends RecyclerView.Adapter implements AdapterLoader<T> {

    private View loadMore;
    private int loadState = STATE_LOADING;

    public SwipeRefreshRecycleView.OnRefreshLoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(SwipeRefreshRecycleView.OnRefreshLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private SwipeRefreshRecycleView.OnRefreshLoadMoreListener loadMoreListener;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private int totalCount;

    public RefreshRecycleAdapter() {
        list = new ArrayList<>();
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public final void setList(List<T> data) {
        if (data == null) {
            return;
        }
        if (list != null) {
            list.clear();
        }
        appendList(data);

    }

    @Override
    public final void appendList(List<T> data) {
        int positionStart = list.size();
        list.addAll(data);
        int itemCount = list.size() - positionStart;

        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionStart + 1, itemCount);
        }
    }

    public List<T> list;

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BOTTOM:
                if (loadMore != null) {
                    RecyclerView.ViewHolder holder = onBottomViewHolderCreate(loadMore);
                    if (holder == null) {
                        throw new RuntimeException("You must impl onBottomViewHolderCreate() and return your own holder ");
                    }
                    return holder;
                } else {
                    return new NewBottomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_footer_new, parent, false), loadMoreListener);
                }
            default:
                return onViewHolderCreate(parent, viewType);
        }

    }

    @Override
    public RecyclerView.ViewHolder onBottomViewHolderCreate(View loadMore) {
        return new NewBottomViewHolder(loadMore, loadMoreListener);
    }

    @Override
    public void onBottomViewHolderBind(RecyclerView.ViewHolder holder, int loadState) {

    }

    @Override
    public abstract RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BOTTOM) {
            loadState = loadState == STATE_ERROR ? STATE_ERROR : isHasMore() ? STATE_LOADING : STATE_LASTED;
            if (loadMore != null) {
                try {
                    onBottomViewHolderBind(holder, loadState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ((NewBottomViewHolder) holder).bindDateView(loadState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performClick(v, holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return performLongClick(v, holder.getAdapterPosition());
                }
            });
            onViewHolderBind(holder, position);
        }
    }

    public void performClick(View itemView, int position) {
        if (listener != null) {
            listener.onItemClick(itemView, position);
        }
    }

    @Override
    public boolean performLongClick(View itemView, int position) {
        return longListener != null && longListener.onItemLongClick(itemView, position);
    }

    @Override
    public abstract void onViewHolderBind(RecyclerView.ViewHolder holder, int position);

    @Override
    public final void isLoadingMore() {
        loadState = STATE_LOADING;
        notifyItemRangeChanged(getItemRealCount(), 1);
    }

    @Override
    public final int getItemCount() {
        return list.size() == 0 ? 0 : list.size() + 1;
    }

    @Override
    public int getItemRealCount() {
        return list.size();
    }

    @Override
    public final void setLoadMoreView(@NonNull View view) {
        loadMore = view;
    }

    @Override
    public final int getItemViewType(int position) {
        if (list.size() > 0 && position < list.size()) {
            return getItemViewTypes(position);
        } else {
            return TYPE_BOTTOM;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewTypes(int position) {
        return 0;
    }


    @Override
    public boolean isHasMore() {
        return getItemCount() < totalCount;
    }

    public final void loadMoreError() {
        loadState = STATE_ERROR;
        notifyItemRangeChanged(getItemRealCount(), 1);
    }

    public void onRefresh() {
//        loadState = STATE_LOADING;
    }

    @Nullable
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Nullable
   private OnItemLongClickListener longListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }
}
