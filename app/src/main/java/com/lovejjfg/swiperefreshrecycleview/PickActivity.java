package com.lovejjfg.swiperefreshrecycleview;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.SelectRefreshRecycleAdapter;
import com.lovejjfg.powerrecycle.TouchHelperCallback;
import com.lovejjfg.swiperefreshrecycleview.model.PickedBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.os.Build.VERSION_CODES.KITKAT;

public class PickActivity extends AppCompatActivity {
    private static final String TAG = PickActivity.class.getSimpleName();
    @Bind(R.id.rv_picked)
    RecyclerView mPickRecyclerView;
    @Bind(R.id.rv_unpick)
    RecyclerView mUnpickRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        ButterKnife.bind(this);
        mPickRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mUnpickRecyclerView.setItemAnimator(new DefaultItemAnimator());
        String[] items = getResources().getStringArray(R.array.items);
        String[] unPickItems = getResources().getStringArray(R.array.unPickItems);
        final ArrayList<PickedBean> pickedBeans = new ArrayList<>();
        final ArrayList<PickedBean> unPickBeans = new ArrayList<>();
        for (String s : items) {
            pickedBeans.add(new PickedBean(s));
        }
        for (String s : unPickItems) {
            unPickBeans.add(new PickedBean(s));
        }
        final PickAdapter pickedAdapter = new PickAdapter();
        final PickAdapter unpickedAdapter = new PickAdapter();
        mPickRecyclerView.setAdapter(pickedAdapter);
        mPickRecyclerView.bringToFront();
        if (Build.VERSION.SDK_INT< KITKAT ) {
            mPickRecyclerView.requestLayout();
            mPickRecyclerView.invalidate();
        }
//        mPickRecyclerView.invalidate();
//        pickedAdapter.setSelectedMode(MultipleMode);
        //初始化一个TouchHelperCallback
        TouchHelperCallback callback = new TouchHelperCallback();
        //添加一个回调
        callback.setItemDragSwipeCallBack(pickedAdapter);
        //初始化一个ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //关联相关的RecycleView
        itemTouchHelper.attachToRecyclerView(mPickRecyclerView);
        mPickRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        pickedAdapter.setList(pickedBeans);
        mUnpickRecyclerView.setAdapter(unpickedAdapter);
        mUnpickRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        unpickedAdapter.setList(unPickBeans);
        pickedAdapter.setOnItemClickListener(new AdapterLoader.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e(TAG, "onItemClick: " + position);
                PickedBean bean = pickedAdapter.list.get(position);
                pickedAdapter.removeItem(position);
                unpickedAdapter.addItem(unpickedAdapter.getItemRealCount(), bean);
            }
        });
        unpickedAdapter.setOnItemClickListener(new AdapterLoader.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.e(TAG, "onItemClick: " + position);
                PickedBean bean = unpickedAdapter.list.get(position);
                unpickedAdapter.removeItem(position);
                pickedAdapter.addItem(pickedAdapter.getItemRealCount(), bean);
            }
        });

    }

    static class PickAdapter extends SelectRefreshRecycleAdapter<PickedBean> {

        @Override
        public RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
            return new PickHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_select, parent, false));
        }

        @Override
        public void onViewHolderBind(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof PickHolder) {
                ((PickHolder) holder).onBind((PickHolder) holder, list.get(position));
            }
        }

        public void removeItem(int position) {
            list.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            return toPosition != 0 && super.onItemMove(fromPosition, toPosition);
        }

        @NonNull
        @Override
        public int[] getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return new int[]{ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_IDLE};
        }

        public void addItem(int position, PickedBean bean) {
            list.add(position, bean);
            notifyItemInserted(position);
        }
    }

    static class PickHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text)
        CheckedTextView mText;

        public PickHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(PickHolder holder, PickedBean bean) {
            mText.setText(bean.title);
            mText.setChecked(bean.isSelected());
        }

    }

//    static class MyTouchHelperCallback extends TouchHelperCallback {
//        @Override
//        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
////            viewHolder.itemView.bringToFront();
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
//    }


}
