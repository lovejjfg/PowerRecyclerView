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

package com.lovejjfg.swiperefreshrecycleview

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.lovejjfg.powerrecycle.AdapterLoader
import com.lovejjfg.powerrecycle.OnLoadMoreListener
import com.lovejjfg.powerrecycle.SelectPowerAdapter
import com.lovejjfg.powerrecycle.SpacesItemDecoration
import com.lovejjfg.powerrecycle.TouchHelperCallback
import com.lovejjfg.powerrecycle.model.ISelect
import com.lovejjfg.swiperefreshrecycleview.model.TestBean
import java.util.ArrayList

class SecondActivity : AppCompatActivity(), OnLoadMoreListener {

    lateinit var mRecycleView: RecyclerView
    lateinit var mRefresh: SwipeRefreshLayout
    lateinit var mToolBar: Toolbar
    private var adapter: SelectPowerAdapter<TestBean>? = null

    private var list: MutableList<TestBean>? = null
    private var refreshAction: Runnable? = null
    private var loadMoreAction: Runnable? = null
    private var isRun: Boolean = false
    private var enable = true
    private var decor: SpacesItemDecoration? = null
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sed)
        mRecycleView = findViewById(R.id.recycle_view)
        mRefresh = findViewById(R.id.srl)
        mToolBar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolBar)
        adapter = SelectRecycleAdapter()
        adapter!!.setOnItemSelectListener(object : AdapterLoader.OnItemSelectedListener {
            override fun onItemSelected(view: View, position: Int, isSelected: Boolean) {
                Log.e(
                    "TAG",
                    "onItemSelected: " + position + "::" + isSelected + ";;total:" + adapter!!.selectedBeans.size
                )
                if (isSelected) {
                    showToast("current：" + position + ";;total:" + adapter!!.selectedBeans.size)
                }
            }

            override fun onNothingSelected() {
                Log.e("TAG", "onNothingSelected: ")
                showToast("一个都没选中：" + adapter!!.selectedBeans.size)
            }
        })
        adapter!!.setOnItemClickListener { itemView, position, item -> showToast("click：$position") }
        adapter!!.updateSelectMode(false)
        //        HashSet<TestBean> selectedBeans = adapter.getSelectedBeans();
        val manager = GridLayoutManager(this, 3)
        mRecycleView!!.layoutManager = manager
        //mRecycleView.setHasFixedSize(true);

        decor = SpacesItemDecoration.Builder(50, 3, true)
            .setShowTopBottom(true)
            .create()
        mRecycleView!!.addItemDecoration(decor)
        //mRecycleView.setItemAnimator(new DefaultAnimator());
        adapter!!.attachRecyclerView(mRecycleView!!)
        mRefresh!!.setOnRefreshListener { mRecycleView!!.postDelayed(refreshAction, DEFAULT_TIME.toLong()) }
        //初始化一个TouchHelperCallback
        val callback = TouchHelperCallback()
        //添加一个回调
        callback.setItemDragSwipeCallBack(adapter)
        //初始化一个ItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(callback)
        //关联相关的RecycleView
        itemTouchHelper.attachToRecyclerView(mRecycleView)

        adapter!!.setLoadMoreListener(this)
        adapter!!.totalCount = 10
        refreshAction = Runnable {
            list = ArrayList()
            for (i in 0..39) {
                list!!.add(TestBean("这是$i"))
            }
            adapter!!.setList(list!!)
            mRefresh!!.isRefreshing = false
        }

        loadMoreAction = Runnable {
            val i = (Math.random() * 10).toInt() % 3
            if (i == 0) {
                val arrayList = ArrayList<TestBean>()
                //arrayList.add(new TestBean("ccc1"));
                //arrayList.add(new TestBean("ccc2"));
                //arrayList.add(new TestBean("ccc3"));
                //arrayList.add(new TestBean("ccc4"));
                //arrayList.add(new TestBean("ccc5"));
                adapter!!.appendList(arrayList)
                Log.e("TAG", "run: 正常加载更多！！")
            } else {
                adapter!!.loadMoreError()
                Log.e("TAG", "run: 加载失败！！！")
            }
            isRun = false
        }
        mRefresh!!.isRefreshing = true
        mRecycleView!!.postDelayed(refreshAction, DEFAULT_TIME.toLong())
    }

    private fun showToast(msg: String) {
        Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onLoadMore() {
        if (isRun) {
            Log.e("TAG", "onLoadMore:正在执行，直接返回。。。 ")
            return
        }
        Log.e("TAG", "onLoadMore: ")
        isRun = true
        mRecycleView!!.postDelayed(loadMoreAction, DEFAULT_TIME.toLong())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.second, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.load_more -> adapter!!.setTotalCount(500)
            R.id.own -> adapter!!.setLoadMoreView(R.layout.layout_foot_self)
            R.id.pull_refresh -> {
                enable = !enable
                mRefresh!!.isEnabled = enable
            }
            R.id.select_single -> {
                adapter!!.setSelectedMode(ISelect.SINGLE_MODE)
                adapter!!.setCurrentPos(3)
            }
            R.id.select_mul -> {
                adapter!!.setSelectedMode(ISelect.MULTIPLE_MODE)
                adapter!!.selectAll()
            }
            R.id.showedge -> {
                mRecycleView!!.removeItemDecoration(decor)
                if (flag) {
                    decor = SpacesItemDecoration.Builder(30, 3, true)
                        .setShowTopBottom(true)
                        .create()
                    flag = false
                } else {
                    decor = SpacesItemDecoration.Builder(30, 3, false)
                        .setShowTopBottom(true)
                        .create()
                    flag = true
                }
                mRecycleView!!.addItemDecoration(decor)
            }
            R.id.normal -> startActivity(Intent(this, NormalActivity::class.java))
            R.id.showNoData -> adapter!!.enableLoadMore(!adapter!!.enableLoadMore)
            R.id.error -> try {
                val inflate = LayoutInflater.from(this).inflate(R.layout.layout_error, mRecycleView, false)
                inflate.findViewById<View>(R.id.iv_empty)
                    .setOnClickListener { v -> Log.e("TAG", "onOptionsItemSelected: 点击了！！") }
                adapter!!.setErrorView(inflate)
                adapter!!.showError(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            R.id.empty -> {
                val inflate = LayoutInflater.from(this).inflate(R.layout.layout_empty, mRecycleView, false)
                inflate.findViewById<View>(R.id.iv_empty)
                    .setOnClickListener { v -> Log.e("TAG", "onOptionsItemSelected: 点击了！！") }
                adapter!!.setEmptyView(inflate)
                adapter!!.showEmpty()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (adapter!!.isSelectMode) {
            adapter!!.updateSelectMode(false)
            showToast("已推出选择模式")
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private val DEFAULT_TIME = 1000
    }
}
