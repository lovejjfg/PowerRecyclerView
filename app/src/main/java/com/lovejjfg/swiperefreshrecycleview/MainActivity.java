package com.lovejjfg.swiperefreshrecycleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lovejjfg.powerrecycle.SwipeRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshRecycleView.OnRefreshLoadMoreListener {

    @Bind(R.id.recycle_view)
    SwipeRefreshRecycleView mRecycleView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    private MyRecycleAdapter adapter;

    private List<String> list;
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
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(adapter);
        mRecycleView.setOnRefreshListener(this);
        adapter = new MyRecycleAdapter();
        adapter.setLoadMoreListener(this);
        adapter.setTotalCount(10);
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("  ");
        }
        refreshAction = new Runnable() {
            @Override
            public void run() {
                adapter.setList(list);
                mRecycleView.setRefresh(false);
            }
        };

        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                int i = ((int) (Math.random() * 10)) % 3;
                if (i == 0 || i == 1) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("ccc1");
                    arrayList.add("ccc2");
                    arrayList.add("ccc3");
                    arrayList.add("ccc4");
                    arrayList.add("ccc5");
                    adapter.appendList(arrayList);
                    Log.e("TAG", "run: 正常加载更多！！");
                } else {
                    adapter.loadMoreError();
                    Log.e("TAG", "run: 加载失败！！！");
                }
                isRun = false;

            }
        };
        mRecycleView.setRefresh(true);
        mRecycleView.postDelayed(refreshAction, DEFAULT_TIME);

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
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.load_more:
                adapter.setTotalCount(100);
                break;
            case R.id.own:
//                adapter.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.recycler_footer_new, mRecycleView, false));
                break;
            case R.id.pull_refresh:
                enable = !enable;
                mRecycleView.setPullRefreshEnable(enable);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
