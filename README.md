

### SwipeRefreshRecycleView使用

![PullRefresh.gif](https://raw.githubusercontent.com/lovejjfg/screenshort/36773514d42e8f7cd546ffbdcd1df4b4212b2f47/PullRefresh.gif)

    //gradle
    compile 'com.lovejjfg.powerrecycle:powerrecycle:1.0.0'

创建：（Adapter 继承 RefreshRecycleAdapter<T>）

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(adapter);
        mRecycleView.setOnRefreshListener(this);
        adapter = new MyRecycleAdapter();
        adapter.setLoadMoreListener(this);
        adapter.setTotalCount(10);

1、创建自己对应的布局的方法：

    RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);

    void onViewHolderBind(RecyclerView.ViewHolder holder, int position);

2、加载更多的几种状态的更改：

    boolean isHasMore();//是否还有更多，可以自己实现具体的逻辑！

    void isLoadingMore();

    void loadMoreError();

3、自定义加载更多的布局：

    void setLoadMoreView(View view);

    RecyclerView.ViewHolder onBottomViewHolderCreate(View loadMore);

    void onBottomViewHolderBind(RecyclerView.ViewHolder holder, int loadState);

4、添加数据源相关的方法，提供了set和append两种方式：

    void setList(List<T> data);

    void appendList(List<T> data);

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

### 博客地址

[戳我戳我](http://www.jianshu.com/p/7396dc6d67f0#)


### 相关问题

1. 使用GridLayoutManager spanCount>1的时候加载更多的布局没有适配。。



