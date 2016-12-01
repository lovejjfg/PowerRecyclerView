package com.lovejjfg.powerrecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lovejjfg.powerrecycle.annotation.LoadState;
import com.lovejjfg.powerrecycle.holder.NewBottomViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joe on 2016-03-11
 * Email: lovejjfg@gmail.com
 */
public abstract class RefreshRecycleAdapter<T> extends RecyclerView.Adapter implements AdapterLoader<T>, TouchHelperCallback.ItemDragSwipeCallBack {

    private View loadMore;
    private int loadState;
    @Nullable
    OnItemLongClickListener longClickListener;
    @Nullable
    OnItemClickListener clickListener;
    @Nullable
    OnItemSelectedListener selectedListener;

    public PowerRecyclerView.OnRefreshLoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(PowerRecyclerView.OnRefreshLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private PowerRecyclerView.OnRefreshLoadMoreListener loadMoreListener;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        notifyDataSetChanged();
    }

    private int totalCount;

    RefreshRecycleAdapter() {
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
        return null;
    }

    @Override
    public void onBottomViewHolderBind(RecyclerView.ViewHolder holder, @LoadState int loadState) {

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
        if (clickListener != null) {
            clickListener.onItemClick(itemView, position);
        }
    }

    @Override
    public boolean performLongClick(View itemView, int position) {
        return longClickListener != null && longClickListener.onItemLongClick(itemView, position);
    }

    @Override
    public abstract void onViewHolderBind(RecyclerView.ViewHolder holder, int position);

    @Override
    public final void isLoadingMore() {
        if (loadState == STATE_LOADING) {
            Log.e("TAG", "isLoadingMore: 正在加载中 !!");
            return;
        }
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


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition == list.size() || toPosition == list.size()) {
            return false;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    void attachToRecyclerView(PowerRecyclerView recycleView) {
        longClickListener = recycleView.getLongClickListener();
        clickListener = recycleView.getClickListener();
        selectedListener = recycleView.getSelectedListener();
    }
}
