
### PowerRecyclerView使用

![PullRefresh.gif](https://raw.githubusercontent.com/lovejjfg/screenshort/master/PullRefresh3.gif)


[![Build Status](https://travis-ci.org/lovejjfg/PowerRecyclerView.svg?branch=master)](https://travis-ci.org/lovejjfg/PowerRecyclerView)
[![API](https://img.shields.io/badge/API-7%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=7)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PowerRecycerView-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4745)
 [ ![Download](https://api.bintray.com/packages/lovejjfg/maven/powerRecycleView/images/download.svg) ](https://bintray.com/lovejjfg/maven/powerRecycleView/_latestVersion)

    //gradle
    compile 'com.lovejjfg.powerrecycle:powerrecycle:1.0.1'

### V1.0.0

* 实现下拉刷新加载更多

### V1.0.1

* 加载更多适配`GridLayoutManager`

* 添加单选和多选模式

### V1.0.2

* 更名 `SwipRefreshRecycleView` 为 `PowerRecyclerView`
* **如果不需要下拉刷新，`Adapter` 可直接适配 `RecycleView`**
* 可直接通过 `PowerRecyclerView` 设置点击、选择事件的回调
* 水平方向的 `ScrollBar` 修复
* 拖拽排序

--------

0、创建：（Adapter 继承 RefreshRecycleAdapter<T>）

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

5、点击 选择模式，及相关回调：

    mRecycleView.setOnItemClickListener(new AdapterLoader.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            toast.setText("点击了：" + position);
            toast.show();
        }
    });

    adapter.setSelectedMode(AdapterLoader.SingleMode);
    adapter.updateSelectMode(true);
    adapter.setOnItemSelectListener(new AdapterLoader.OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, boolean isSelected) {
            Log.e("TAG", "onItemSelected: " + position + "::" + isSelected);
        }

        @Override
        public void onNothingSelected() {
            Log.e("TAG", "onNothingSelected: ");
        }
    });

6、GridLayoutManager设置SpanSize

        mRecycleView.setSpanSizeCallBack(new SwipeRefreshRecycleView.SpanSizeCallBack() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });


7、使用SpacesItemDecoration添加上下左右留白

    /**
     *
     * @param space item之间的空间
     * @param count 列数
     * @param showEdge 是否显示左右边缘
     */
    public SpacesItemDecoration(int space, int count, boolean showEdge) {
        this.spacing = space;
        this.spanCount = count;
        this.showEdge = showEdge;
        pre = spacing * 1.0f / spanCount;
    }

    decor = new SpacesItemDecoration(30, 3, true);
    mRecycleView.getRecycle().addItemDecoration(decor);

8、拖拽排序

        //初始化一个TouchHelperCallback
        TouchHelperCallback callback = new TouchHelperCallback();
        //添加一个回调
        callback.setItemDragSwipeCallBack(adapter);
        //初始化一个ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //关联相关的RecycleView
        itemTouchHelper.attachToRecyclerView(mRecycleView.getRecycle());

9、自定义加载更多

	//1、指定对应布局
    adapter.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.layout_foot_self, mRecycleView, false));

	//2、创建对应的Holder 实现Adapter中的两个方法
    @Override
    public RecyclerView.ViewHolder onBottomViewHolderCreate(View loadMore) {
        return new BottomViewHolder(loadMore);
    }

    @Override
    public void onBottomViewHolderBind(RecyclerView.ViewHolder holder, int loadState) {
        ((BottomViewHolder) holder).onBind(getLoadMoreListener(), loadState);
    }

PS:**直接使用RecyclerView请注意添加`mRecycleView.setItemAnimator(new DefaultAnimator());`**


### 博客地址

[Android RecycleView轻松实现下拉刷新、加载更多](http://www.jianshu.com/p/7396dc6d67f0#)
[Android RecyclerView 定制单选多选模式](http://www.jianshu.com/p/8026bcbd290f)






