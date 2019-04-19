
### PowerRecyclerView使用

![PullRefresh.gif](https://raw.githubusercontent.com/lovejjfg/screenshort/master/PullRefresh3.gif)


[![Build Status](https://travis-ci.org/lovejjfg/PowerRecyclerView.svg?branch=master)](https://travis-ci.org/lovejjfg/PowerRecyclerView)
[![API](https://img.shields.io/badge/API-7%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=7)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PowerRecycerView-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4745)
 [ ![Download](https://api.bintray.com/packages/lovejjfg/maven/powerRecycleView/images/download.svg) ](https://bintray.com/lovejjfg/maven/powerRecycleView/_latestVersion)

### 快速使用

    implementation 'com.lovejjfg.powerrecycle:powerrecycle:lastedVersion'

因为 lib 已经依赖了 v7 和 `RecyclerView` ,当前版本是 27 ，可以移除。

    implementation ('com.lovejjfg.powerrecycle:powerrecycle:1.2.6'){
        exclude group: 'com.android.support', module: 'appcompat-v7'
        exclude group: 'com.android.support', module: 'recyclerview-v7'
    }



### V1.0.0

* 实现下拉刷新加载更多

### V1.0.1

* 加载更多适配`GridLayoutManager`

* 添加单选和多选模式

### V1.0.2

* 更名 `SwipRefreshRecycleView` 为 `PowerRecyclerView`
* 更名 `RefreshRecycleAdapter` 为 `PowerAdapter`
* **如果不需要下拉刷新 ，`Adapter` 可直接适配 `RecycleView`**
* 可直接通过 `PowerRecyclerView` 设置点击、选择事件的回调
* 水平方向的 `ScrollBar` 修复
* 修复拖拽排序存在的bug
* 增加移除某个Item和添加Item的方法

### V1.0.3

* `PowerAdapter` 修复构造方法

### V1.2.0

* 废除了 `PowerRecyclerView` ,如果需要使用 `SwipeRefreshLayout` 请自己手动添加，聚合比组合更加灵活，如果需要自定义下拉刷新 `Header` ,可以使用
[PowerRefresh](https://github.com/lovejjfg/PowerRefresh)  
* 在点击回调方法中增加Item的返回。
* 增加了 `PowerHolder` 作为默认的 `Holder`
* 增加设置 错误页面 和 空白页面 的两个方法

### V1.2.6
* 移除 lambda
* `setLoadMoreView()` 方法参数改为接收 布局资源id
* `LoadMoreScrollListener` 在 `attachRecyclerView()` 中自动设置，并不对外暴露
* `onBottomViewHolderBind()` 修改，增加 listener
* DefaultAnimator 废除，理论上不需要再设置这个解决相关crash。

### V2.0.0

#### bugfix:
1. 默认 loadingmore 的 loading 状态显示异常
2. 调用 `updateLoadingMore()` 方法更新 加载更多 状态异常
3. Kotlin 中，data class 使用时，选择状态异常
4. 加载更多刷新状态异常


#### `PowerAdapter` changes:
1. `attachRecyclerView()` 废除，直接使用 `recyclerView.setAdapter()` 即可
2. `setLoadMoreView()` 废除，新增 `createLoadMoreView()`
3. `setEmptyView()` 废除，新增 `createEmptyView()`
4. `setErrorView()` 废除，新增 `createErrorView()`
5. 新增 `onViewHolderBind(@NonNull PowerHolder<T> holder, int position, @NonNull List<Object> payloads)` 支持局部刷新
6. itemClick itemLongClick 回调方法参数调整（view -> PowerHolder）
7. 新增 `updateItem()` 用于刷新 Item.
8. enableLoadMore 默认为 true 并且私有化

#### `SelectPowerAdapter` changes:
0. 实现接口 `AdapterSelect`
1. `OnItemSelectedListener.onItemSelectChange(@NonNull PowerHolder<S> holder, int position, boolean isSelected)`参数变化
2. 新增 `setCurrentPos(int position)`  `setCurrentPositions(@NonNull int... position)` 两方法设置默认选中 `items`(**注意，setList() 方法目前并不会直接触发选择状态**)
3. 新增 `getMaxSelectCount()` 和 `onReceivedMaxSelectCount(int count)` 控制最大选择数量
4. 新增 `deleteSelectedItems()`  移除选中items
5. 新增 `revertAllSelected()` 反选 items 反选上限受 `getMaxSelectCount()` 影响

#### `PowerHolder` changes:
1. 新增 `onPartBind(@NonNull T t, @NonNull List<Object> payloads)` 和 `onPartBind(@NonNull T t, boolean isSelectMode, @NonNull List<Object> payloads)` 用于局部数据刷新


## Examples

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

~~PS:**直接使用RecyclerView请注意添加`mRecycleView.setItemAnimator(new DefaultAnimator());`**~~

~~因为有使用 lambda ，所以你可能需要指定 Java 的版本:~~

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }


### 混淆

    -dontwarn com.lovejjfg.**


### 博客地址

[Android RecycleView轻松实现下拉刷新、加载更多](http://www.jianshu.com/p/7396dc6d67f0#)

[Android RecyclerView 定制单选多选模式](http://www.jianshu.com/p/8026bcbd290f)

[Android 打造专属的下拉刷新 加载更多](http://www.jianshu.com/p/e46f72ce69c4)

[编译不过？！](https://github.com/lovejjfg/PowerRecyclerView/issues/1)

### 项目使用

[Readhub](https://github.com/lovejjfg/Readhub)




