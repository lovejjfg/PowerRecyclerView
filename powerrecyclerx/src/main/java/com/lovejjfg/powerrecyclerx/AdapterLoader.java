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

package com.lovejjfg.powerrecyclerx;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Created by Joe on 2016-07-26
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings("unused")
public interface AdapterLoader<T> {

    /**
     * state about load more..
     */
    int STATE_LOADING = 1;
    int STATE_LASTED = 2;
    int STATE_ERROR = 3;

    int TYPE_BOTTOM = 0x80000000;
    int TYPE_EMPTY = 0x80000001;
    int TYPE_ERROR = 0x80000010;

    String PAYLOAD_REFRESH_SELECT = "SelectPowerAdapter$refresh_select";
    String PAYLOAD_REFRESH_BOTTOM = "PowerAdapter$refresh_bottom";

    /**
     * This method should be called  when you load more !
     *
     * @param holder    the current holder.
     * @param loadState the current state.
     */
    void onBottomViewHolderBind(@NonNull AbsBottomViewHolder holder,
        @NonNull OnLoadMoreListener listener,
        @LoadState int loadState);

    /**
     * If you want to create the specified bottom layout,you must call this method to add your specified layout !
     * consider to use {@link AdapterLoader#createLoadMoreView(ViewGroup)}
     *
     * @param viewRes the specified bottom view layout res
     */
    @Deprecated
    void setLoadMoreView(@LayoutRes int viewRes);

    @Nullable
    View createLoadMoreView(@NonNull ViewGroup parent);

    /**
     * consider to use {@link AdapterLoader#createEmptyView(ViewGroup)}
     *
     * @param emptyView the emptyView to show
     */
    @Deprecated
    void setEmptyView(@NonNull View emptyView);

    /**
     * create the Empty
     * <code> LayoutInflater.from(parent.getContext()).inflate(R.layout.xxx, parent, false); </code>
     */
    @Nullable
    View createEmptyView(@NonNull ViewGroup parent);

    /**
     * consider to use {@link AdapterLoader#createErrorView(ViewGroup)}
     *
     * @param errorView the errorView to show
     */
    @Deprecated
    void setErrorView(@NonNull View errorView);

    @Nullable
    View createErrorView(@NonNull ViewGroup parent);

    /**
     * show the empty view when data is empty.
     */
    void showEmpty();

    /**
     * show the error view when load data error.
     *
     * @param force true would show the error view don't care there was data before.false would care about.
     */
    void showError(boolean force);

    /**
     * If you want to create the specified bottom layout ,you should implements this method to create your own
     * bottomViewHolder
     *
     * @param loadMore whether is loadingMore or not..
     */
    @Nullable
    AbsBottomViewHolder onBottomViewHolderCreate(@NonNull View loadMore);

    /**
     * {@link PowerAdapter} through this method to known whether there is more data or nor.
     *
     * @return true if there is more data,  otherwise false.
     */
    boolean isHasMore();

    /**
     * refresh the bottom holder state ,this method is always called from the {@link LoadMoreScrollListener}
     */
    void updateLoadingMore();

    void loadMoreError();

    /**
     * call this method to set whether there is support loadmore or not.
     *
     * @param loadMore true: support load more false: not show load more anymore
     */
    void enableLoadMore(boolean loadMore);

    boolean isEnableLoadMore();

    /**
     * Called by RecyclerView to display the error view.
     */
    void onErrorHolderBind(@NonNull PowerHolder<T> holder);

    /**
     * Called by RecyclerView to display the empty view.
     */
    void onEmptyHolderBind(@NonNull PowerHolder<T> holder);

    /**
     * You can call this method to add data to RecycleView,if you want to append data,you should call
     * {@link #appendList(List)}, Maybe You should call setTotalCount() before setList() .
     *
     * @param data the data you want to add
     */
    void setList(@NonNull List<T> data);

    /**
     * clear current data.
     */
    void clearList();

    /**
     * clear current data.
     */
    void clearList(boolean notify);

    /**
     * @param data the data you want to add.
     */
    void appendList(@NonNull List<T> data);

    /**
     * insert the new list data within the start Position.
     *
     * @param data     new list to insert
     * @param startPos the start position
     */
    void insertList(@NonNull List<T> data, int startPos);

    /**
     * check the position is illegal.
     *
     * @return true the position is out of [0,list.size()-1] ,else legal.
     */
    boolean checkIllegalPosition(int position);

    /**
     * remove the specified position in the list. If this method throw RecyclerView Exception when you delete the
     * last one.
     * consider about using the {@link com.lovejjfg.powerrecyclerx.manager.FixedLinearLayoutManager} or
     * {@link com.lovejjfg.powerrecyclerx.manager.FixedGridLayoutManager} to avoid crash when you want to delete the lasted item.
     *
     * @param position he specified position to remove
     * @return if successful return the removed object,otherwise null
     */
    @Nullable
    T removeItem(int position);

    /**
     * call this method to update the item Holder's itemView.
     *
     * @return true find the item in current list, false otherwise.
     */
    boolean updateItem(@NonNull T item);

    /**
     * call this method to update the item Holder's itemView.
     *
     * @return true find the item in current list, false otherwise.
     */
    boolean updateItem(@NonNull T item, @Nullable Object payload);

    /**
     * get the data of list in this RecyclerView.
     * return the data in specified position, or null.
     *
     * @param position the specified position
     */
    @Nullable
    T getItem(int position);

    /**
     * remove the specified position in the list.
     *
     * @param position he specified position to remove
     */
    void insertItem(int position, @NonNull T item);

    /**
     * @param position the current pos .
     * @return the current Type.
     */
    int getItemViewTypes(int position);

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the PowerHolder#itemView to reflect the item at
     * the given position.
     *
     * @param holder   current holder.
     * @param position current pos.
     */
    void onViewHolderBind(@NonNull PowerHolder<T> holder, int position);

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the PowerHolder#itemView to reflect the item at
     * the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     *                 update.
     */
    void onViewHolderBind(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads);

    /**
     * Called when RecyclerView needs a new {@link PowerHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onViewHolderBind(PowerHolder, int, List)} (ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    PowerHolder<T> onViewHolderCreate(@NonNull ViewGroup parent, int viewType);

    /**
     * Return the current size about {@link PowerAdapter#list}.
     *
     * @return current list size!
     */
    int getItemRealCount();

    /**
     * handle the holder itemview click.
     *
     * @param holder   the holder that click
     * @param position the position of holder in RecyclerView
     * @param item     the data of holder.
     */
    void performClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item);

    /**
     * handle the holder itemview long click.
     *
     * @param holder   the holder that click
     * @param position the position of holder in RecyclerView
     * @param item     the data of holder.
     */
    boolean performLongClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item);

    /**
     * call this method after init RecyclerView (set LayoutManager at first).
     * <p><b>Deprecated in v2.0.0 ,you just need to  call recyclerView.setAdapter() as usually. But you should
     * always setup LayoutManager at first.</b></p>
     */
    @Deprecated
    void attachRecyclerView(@NonNull RecyclerView recyclerView);

    /**
     * call this method to get the first position of the viewType
     *
     * @param viewType the viewType you set ,default is 0
     * @return the index of list
     */
    int findFirstPositionOfType(int viewType);

    /**
     * call this method to get the first position of the viewType start with the offsetPosition end to list end.
     *
     * @param viewType       the viewType you set ,default is 0
     * @param offsetPosition the position to offset.
     * @return the index of list
     */
    int findFirstPositionOfType(int viewType, int offsetPosition);

    /**
     * call this method to get the last position of the viewType
     *
     * @param viewType the viewType you set ,default is 0
     * @return the index of list
     */
    int findLastPositionOfType(int viewType);

    /**
     * call this method to get the last position of the viewType start with the offsetPosition end to 0.
     *
     * @param viewType       the viewType you set ,default is 0
     * @param offsetPosition the position to offset.
     * @return the index of list
     */
    int findLastPositionOfType(int viewType, int offsetPosition);

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    void setOnItemClickListener(@Nullable OnItemClickListener<T> listener);

    /**
     * Interface definition for a callback to be invoked when an item in this
     * Adapter has been clicked.
     */
    interface OnItemClickListener<T> {
        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param holder   The holder within the RecyclerView that was clicked
         * @param position The position of the view in the adapter.
         * @param item     The data of holder that bind.
         */
        void onItemClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item);
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked.
     *
     * @param listener The callback that will be invoked.
     */
    void setOnItemLongClickListener(@Nullable OnItemLongClickListener<T> listener);

    /**
     * Interface definition for a callback to be invoked when an item in this
     * Adapter has been long clicked.
     */
    interface OnItemLongClickListener<T> {
        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been long clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param holder   The holder within the RecyclerView that was clicked
         * @param position The position of the view in the adapter.
         * @param item     The data of holder that bind.
         */
        boolean onItemLongClick(@NonNull PowerHolder<T> holder, int position, @NonNull T item);
    }

    /**
     * Register a callback to be invoked when RecyclerView error layout has been clicked.
     *
     * @param errorClickListener The callback that will be invoked.
     */
    void setErrorClickListener(@Nullable OnErrorClickListener errorClickListener);

    /**
     * Interface definition for a callback to be invoked when error layout click
     */
    interface OnErrorClickListener {
        void onErrorClick(@NonNull View view);
    }
}
