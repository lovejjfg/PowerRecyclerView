package com.lovejjfg.powerrecycle;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.lovejjfg.powerrecycle.holder.NewBottomViewHolder;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static com.lovejjfg.powerrecycle.AdapterLoader.TYPE_BOTTOM;


/**
 * Created by Joe on 2016-03-28
 * Email: lovejjfg@163.com
 */
public class TouchHelperCallback extends ItemTouchHelper.Callback {
    //        int swipeFlags =  ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        return makeMovementFlags(0, swipeFlags);
    @SuppressWarnings("ConstantConditions")
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == TYPE_BOTTOM) {
            return makeMovementFlags(0, 0);
        }
        if (callBack != null) {
            int[] flags = callBack.getMovementFlags(recyclerView, viewHolder);
            if (flags == null || flags.length != 2) {
                throw new IllegalStateException("method getMovementFlags() should return type int[]  witch length mush be 2 ,by default you can call super.getMovementFlags().");
            }
            return makeMovementFlags(flags[0], flags[1]);
        }
        return makeMovementFlags(0, 0);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return callBack != null && callBack.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (callBack != null && !(viewHolder instanceof NewBottomViewHolder)) {
            callBack.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    }

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
                            boolean isCurrentlyActive) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            float width = (float) viewHolder.itemView.getWidth();
//            float alpha = 1.0f - Math.abs(dX) / width;
//            viewHolder.itemView.setAlpha(alpha);
//        } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
//            if (isCurrentlyActive) {
//                viewHolder.itemView.setScaleX(1.2f);
//                viewHolder.itemView.setScaleY(1.2f);
//            } else {
//                viewHolder.itemView.setScaleX(1);
//                viewHolder.itemView.setScaleY(1);
//            }
//        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                actionState, isCurrentlyActive);
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public void setItemDragSwipeCallBack(@Nullable ItemDragSwipeCallBack callBack) {
        this.callBack = callBack;
    }

    @Nullable
    private ItemDragSwipeCallBack callBack;

    public interface ItemDragSwipeCallBack {
        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);

        /**
         * Instead of composing this flag manually, you can use makeMovementFlags(int, int) or makeFlag(int, int).
         * This flag is composed of 3 sets of 8 bits, where first 8 bits are for IDLE state, next 8 bits are for SWIPE state and third 8 bits are for DRAG state. Each 8 bit sections can be constructed by simply OR'ing direction flags defined in ItemTouchHelper.
         * For example, if you want it to allow swiping LEFT and RIGHT but only allow starting to swipe by swiping RIGHT, you can return:
         * <p>
         * <code>new  int[]{ItemTouchHelper.UP | ItemTouchHelper.DOWN |
         * ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_IDLE};</code>
         */
        @NonNull
        int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }


}
