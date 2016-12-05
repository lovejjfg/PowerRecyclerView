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

package com.lovejjfg.swiperefreshrecycleview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.DefaultAnimator;
import com.lovejjfg.powerrecycle.PowerRecyclerView;
import com.lovejjfg.powerrecycle.SelectPowerAdapter;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.lovejjfg.powerrecycle.model.ISelect.MultipleMode;
import static com.lovejjfg.powerrecycle.model.ISelect.SingleMode;

public class MainActivity extends AppCompatActivity implements PowerRecyclerView.OnRefreshLoadMoreListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.recyclerview)
    RecyclerView mRecycleView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    private SelectPowerAdapter<TestBean> adapter;

    private List<TestBean> list;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private boolean enable = true;
    private static final int DEFAULT_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        adapter = new MyRecycleAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(adapter);
        mRecycleView.setItemAnimator(new DefaultAnimator());
        //自定义加载更多
        adapter.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.layout_foot_self, mRecycleView, false));
        adapter.setOnItemSelectListener(new AdapterLoader.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, @ColorInt int position, boolean isSelected) {
                Log.e("TAG", "onItemSelected: " + position + "::" + isSelected);
            }

            @Override
            public void onNothingSelected() {
                Log.e("TAG", "onNothingSelected: ");
            }
        });
        adapter.setOnItemClickListener((v, p) -> Log.e(TAG, "onItemClick: " + p));
        //初始化一个TouchHelperCallback
//        TouchHelperCallback callback = new TouchHelperCallback();
        //添加一个回调
//        callback.setItemDragSwipeCallBack(adapter);
        //初始化一个ItemTouchHelper
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //关联相关的RecycleView
//        itemTouchHelper.attachToRecyclerView(mRecycleView.getRecycle());

        adapter.setLoadMoreListener(this);
        adapter.setTotalCount(10);
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(new TestBean("这是" + i));
        }
        adapter.setList(list);
        loadMoreAction = () -> {
            int i = ((int) (Math.random() * 10)) % 3;
            if (i == 0 || i == 1) {
                ArrayList<TestBean> arrayList = new ArrayList<>();
                arrayList.add(new TestBean("ccc1"));
                arrayList.add(new TestBean("ccc2"));
                arrayList.add(new TestBean("ccc3"));
                arrayList.add(new TestBean("ccc4"));
                arrayList.add(new TestBean("ccc5"));
                adapter.appendList(arrayList);
                Log.e("TAG", "run: 正常加载更多！！");
            } else {
                adapter.loadMoreError();
                Log.e("TAG", "run: 加载失败！！！");
            }
            isRun = false;
        };

    }

    @Override
    public void onRefresh() {
        mRecycleView.postDelayed(refreshAction, DEFAULT_TIME);

    }

    @Override
    public void onLoadMore() {
        if (isRun) {
            return;
        }
        Log.e("TAG", "onLoadMore: ");
        isRun = true;
        mRecycleView.postDelayed(loadMoreAction, DEFAULT_TIME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //loadMore
            case R.id.load_more:
                adapter.setTotalCount(50);
                break;
            //yourself LoadMoreView you must impl onBottomViewHolderCreate() and onBottomViewHolderBind() in your adapter!
            case R.id.own:

                break;
            //enable pullRefresh
            case R.id.pull_refresh:
                enable = !enable;
//                mRecycleView.setPullRefreshEnable(enable);
                break;
            //singleChoiceMode
            case R.id.select_single:
                adapter.setSelectedMode(SingleMode);
                adapter.updateSelectMode(true);
                break;
            //multipleChoiceMode
            case R.id.select_mul:
                adapter.setSelectedMode(MultipleMode);
                adapter.updateSelectMode(true);
                break;
            case R.id.power:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (adapter.isSelectMode()) {
            adapter.updateSelectMode(false);
            return;
        }
        super.onBackPressed();
    }
}
