package com.lovejjfg.swiperefreshrecycleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.SelectRefreshRecycleAdapter;
import com.lovejjfg.powerrecycle.SpacesItemDecoration;
import com.lovejjfg.powerrecycle.SwipeRefreshRecycleView;
import com.lovejjfg.swiperefreshrecycleview.model.TestBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.lovejjfg.powerrecycle.AdapterLoader.TYPE_BOTTOM;

public class SecondActivity extends AppCompatActivity implements SwipeRefreshRecycleView.OnRefreshLoadMoreListener {

    @Bind(R.id.recycle_view)
    SwipeRefreshRecycleView mRecycleView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    private SelectRefreshRecycleAdapter<TestBean> adapter;

    private List<TestBean> list;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private boolean enable = true;
    private static final int DEFAULT_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sed);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        mToolBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NotificationUtil.sendSimplestNotificationWithAction(SecondActivity.this);
                return true;
            }
        });
        adapter = new SelectRecycleAdapter();
        adapter.setOnItemSelectListener(new AdapterLoader.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, boolean isChecked) {
                Log.e("TAG", "onItemSelected: " + position + "::" + isChecked);
            }

            @Override
            public void onNothingSelected() {
                Log.e("TAG", "onNothingSelected: ");
            }
        });
        adapter.setOnItemClickListener(new AdapterLoader.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int postion) {

            }
        });
//        HashSet<TestBean> selectedBeans = adapter.getSelectedBeans();
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setSpanSizeCallBack(new SwipeRefreshRecycleView.SpanSizeCallBack() {
            @Override
            public int getSpanSize(int position) {
                return 0;
            }
        });

        SpacesItemDecoration decor = new SpacesItemDecoration(30, 3, true);
        mRecycleView.getRecycle().addItemDecoration(decor);
        mRecycleView.setAdapter(adapter);
        mRecycleView.setOnRefreshListener(this);
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
            Log.e("TAG", "onLoadMore:正在执行，直接返回。。。 ");
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
            case R.id.select_single:
                adapter.setSelectedMode(AdapterLoader.SingleMode);
                adapter.updateSelectMode(true);

                break;
            case R.id.select_mul:
                adapter.setSelectedMode(AdapterLoader.MultipleMode);
                adapter.updateSelectMode(true);
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
