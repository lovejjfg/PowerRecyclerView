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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.lovejjfg.swiperefreshrecycleview.model.TestBean
import java.util.ArrayList

class NormalActivity : AppCompatActivity() {
    //@BindView(R.id.recyclerview)
    lateinit var mRecycleView: RecyclerView
    //@BindView(R.id.toolbar)
    lateinit var mToolBar: Toolbar
    lateinit var adapter: NormalAdapter

    private var list: MutableList<TestBean>? = null
    private val refreshAction: Runnable? = null
    private var loadMoreAction: Runnable? = null
    private var isRun: Boolean = false
    private val enable = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolBar = findViewById(R.id.toolbar)
        mRecycleView = findViewById(R.id.recyclerview)
        setSupportActionBar(mToolBar)
        adapter = NormalAdapter()
        val manager = GridLayoutManager(this, 2)
        //1.setLayoutManager
        mRecycleView.layoutManager = manager
        //2.setAdapter after setLayoutManager
        adapter.attachRecyclerView(mRecycleView)
        //3.setLoadMoreScrollListener
        adapter.setOnItemClickListener { v, p, item -> Log.e(TAG, "onItemClick: $p") }
        //4.setTotalCount
        adapter.totalCount = 100
        adapter.setLoadMoreListener {
            if (isRun) {
                Log.e("TAG", "onLoadMore:正在执行，直接返回。。。 ")
                return@setLoadMoreListener
            }
            Log.e("TAG", "onLoadMore: ")
            isRun = true
            mRecycleView.postDelayed(loadMoreAction, DEFAULT_TIME.toLong())
        }
        //        mRecycleView.setOnRefreshListener(this);
        this.list = ArrayList()
        for (i in 0..29) {
            this.list!!.add(TestBean("这是$i"))
        }
        //3.initData
        adapter.setList(this.list!!)
        loadMoreAction = Runnable {
            val i = (Math.random() * 10).toInt() % 3
            if (i == 0 || i == 1) {
                val arrayList = ArrayList<TestBean>()
                arrayList.add(TestBean("ccc1"))
                arrayList.add(TestBean("ccc2"))
                arrayList.add(TestBean("ccc3"))
                arrayList.add(TestBean("ccc4"))
                arrayList.add(TestBean("ccc5"))
                adapter.appendList(arrayList)
                Log.e("TAG", "run: 正常加载更多！！")
            } else {
                adapter.loadMoreError()
                Log.e("TAG", "run: 加载失败！！！")
            }
            isRun = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private val TAG = NormalActivity::class.java.simpleName
        private val DEFAULT_TIME = 1000
    }
}
