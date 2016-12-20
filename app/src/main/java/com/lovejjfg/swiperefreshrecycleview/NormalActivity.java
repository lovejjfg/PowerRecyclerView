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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lovejjfg.powerrecycle.LoadMoreScrollListener;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NormalActivity extends AppCompatActivity {
    private static final String TAG = NormalActivity.class.getSimpleName();
    @Bind(R.id.recyclerview)
    RecyclerView mRecycleView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    NormalAdapter adapter;

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
        adapter = new NormalAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //1.setLayoutManager
        mRecycleView.setLayoutManager(manager);
        //2.setAdapter
        mRecycleView.setAdapter(adapter);
//        mRecycleView.setItemAnimator(new DefaultAnimator());
        mRecycleView.addOnScrollListener(new LoadMoreScrollListener(mRecycleView));
        adapter.setOnItemClickListener((v, p) -> Log.e(TAG, "onItemClick: " + p));
//        adapter.setTotalCount(100);
        adapter.setLoadMoreListener(() -> {
            if (isRun) {
                Log.e("TAG", "onLoadMore:正在执行，直接返回。。。 ");
                return;
            }
            Log.e("TAG", "onLoadMore: ");
            isRun = true;
            mRecycleView.postDelayed(loadMoreAction, DEFAULT_TIME);
        });
//        mRecycleView.setOnRefreshListener(this);
        this.list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            this.list.add(new TestBean("这是" + i));
        }
        //3.initData
        adapter.setList(this.list);
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


}
