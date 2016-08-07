package com.lovejjfg.powerrecycle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Joe on 2016-07-26
 * Email: lovejjfg@gmail.com
 */
public interface AdapterLoader<T> {
    /**
     * state about load more..
     */
    int STATE_LOADING = 1;
    int STATE_LASTED = 2;
    int STATE_ERROR = 3;

    int TYPE_BOTTOM = 400;
    /**
     * This method should be called  when you load more !
     */
//    void appendList(T t);

    void onBottomViewHolderBind(RecyclerView.ViewHolder holder, int loadState);
    /**
     * <p>If you want to create the specified bottom layout,you must call this method to add your specified layout !</p>
     * <p>If you don't call this method ,{@link #onBottomViewHolderBind(RecyclerView.ViewHolder, int)} will not call any way!</p>
     * <p>if you call this method you must impl {@link #onBottomViewHolderBind(RecyclerView.ViewHolder, int)} && {@link #onBottomViewHolderCreate(View)}</p>
     * @param view the specified bottom layout
     */
    void setLoadMoreView(View view);
    /**
     * If you want to create the specified bottom layout ,you should implements this method to create your own bottomViewHolder
     * and before this method called ,you should call {@link #setLoadMoreView(View)} to inflate the bottom layout at first !!!
     * @param loadMore  wether is loadingMore or not..
     */
    RecyclerView.ViewHolder onBottomViewHolderCreate(View loadMore);

    void onRefresh();

    boolean isHasMore();

    void isLoadingMore();

    void loadMoreError();

    /**
     * You can call this method to add data to RecycleView,if you want to append data,you should call {@link #appendList(List)}
     *
     * @param data the data you want to add
     */
    void setList(List<T> data);

    void appendList(List<T> data);
    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     *
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     *                 <code>position</code>. Type codes need not be contiguous.
     */
    int getItemViewTypes(int position);
    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    void onViewHolderBind(RecyclerView.ViewHolder holder, int position);

    RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);

    /**
     * Return the current size about {@link RefreshRecycleAdapter#list}.
     * @return current list size!
     */
    int getItemRealCount();


}
