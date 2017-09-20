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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.OnLoadMoreListener;
import com.lovejjfg.powerrecycle.SelectPowerAdapter;
import com.lovejjfg.powerrecycle.SpacesItemDecoration;
import com.lovejjfg.powerrecycle.TouchHelperCallback;
import com.lovejjfg.powerrecycle.model.ISelect;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SecondActivity extends AppCompatActivity implements OnLoadMoreListener {

    @Bind(R.id.recycle_view)
    RecyclerView mRecycleView;
    @Bind(R.id.srl)
    SwipeRefreshLayout mRefresh;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    private SelectPowerAdapter<TestBean> adapter;

    private List<TestBean> list;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private boolean enable = true;
    private static final int DEFAULT_TIME = 1000;
    private SpacesItemDecoration decor;
    private boolean flag;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sed);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mToolBar.setOnLongClickListener(v -> {
            NotificationUtil.sendSimplestNotificationWithAction(SecondActivity.this);
            return true;
        });
        adapter = new SelectRecycleAdapter();
        adapter.setOnItemSelectListener(new AdapterLoader.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, boolean isSelected) {
                Log.e("TAG", "onItemSelected: " + position + "::" + isSelected);
                if (isSelected) {
                    toast.setText(position + "::" + true);
                    toast.show();
                }
            }

            @Override
            public void onNothingSelected() {
                Log.e("TAG", "onNothingSelected: ");
            }
        });
        adapter.setOnItemClickListener((itemView, position, item) -> {
            toast.setText("点击了：" + position);
            toast.show();
        });
//        HashSet<TestBean> selectedBeans = adapter.getSelectedBeans();
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecycleView.setLayoutManager(manager);

        decor = new SpacesItemDecoration(30, 3, true);
        mRecycleView.addItemDecoration(decor);
        adapter.attachRecyclerView(mRecycleView);
        mRefresh.setOnRefreshListener(() -> mRecycleView.postDelayed(refreshAction, DEFAULT_TIME));
        //初始化一个TouchHelperCallback
        TouchHelperCallback callback = new TouchHelperCallback();
        //添加一个回调
        callback.setItemDragSwipeCallBack(adapter);
        //初始化一个ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //关联相关的RecycleView
        itemTouchHelper.attachToRecyclerView(mRecycleView);

        adapter.setLoadMoreListener(this);
        adapter.setTotalCount(10);
        refreshAction = () -> {
            list = new ArrayList<>();
            for (int i = 0; i < 40; i++) {
                list.add(new TestBean("这是" + i));
            }
            adapter.setList(list);
            mRefresh.setRefreshing(false);
        };

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
        mRefresh.setRefreshing(true);
        mRecycleView.postDelayed(refreshAction, DEFAULT_TIME);

    }


    @Override
    public void onLoadMore() {
        if (isRun) {
            Log.e("TAG", "onLoadMore:正在执行，直接返回。。。 ");
            return;
        }
        Log.e("TAG", "onLoadMore: ");
        isRun = true;
        mRecycleView.postDelayed(loadMoreAction, DEFAULT_TIME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.load_more:
                adapter.setTotalCount(500);
                break;
            case R.id.own:
//                adapter.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.recycler_footer_new, mRecycleView, false));
                break;
            case R.id.pull_refresh:
                enable = !enable;
                mRefresh.setEnabled(enable);
                break;
            case R.id.select_single:
                adapter.setSelectedMode(ISelect.SINGLE_MODE);
                adapter.updateSelectMode(true);

                break;
            case R.id.select_mul:
                adapter.setSelectedMode(ISelect.MULTIPLE_MODE);
                adapter.updateSelectMode(true);
                break;
            case R.id.showedge:
                mRecycleView.removeItemDecoration(decor);
                if (flag) {
                    decor = new SpacesItemDecoration(30, 3, true);
                    flag = false;
                } else {
                    decor = new SpacesItemDecoration(30, 3, false);
                    flag = true;
                }
                mRecycleView.addItemDecoration(decor);
                break;
            case R.id.normal:
                startActivity(new Intent(this, NormalActivity.class));
                break;
            case R.id.showNoData:
                adapter.enableLoadMore(!adapter.enableLoadMore);
                break;
            case R.id.error:
                try {
                    View inflate = LayoutInflater.from(this).inflate(R.layout.layout_error, mRecycleView, false);
                    inflate.findViewById(R.id.iv_empty).setOnClickListener((v -> Log.e("TAG", "onOptionsItemSelected: 点击了！！")));
                    adapter.setErrorView(inflate);
                    adapter.showError(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.empty:
                View inflate = LayoutInflater.from(this).inflate(R.layout.layout_empty, mRecycleView, false);
                inflate.findViewById(R.id.iv_empty).setOnClickListener((v -> Log.e("TAG", "onOptionsItemSelected: 点击了！！")));
                adapter.setEmptyView(inflate);
                adapter.showEmpty();
                break;
            default:
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
