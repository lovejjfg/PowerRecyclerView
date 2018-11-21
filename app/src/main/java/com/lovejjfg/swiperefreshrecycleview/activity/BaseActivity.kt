package com.lovejjfg.swiperefreshrecycleview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.LayoutManager
import android.view.MenuItem
import androidx.core.widget.toast
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.swiperefreshrecycleview.R
import kotlinx.android.synthetic.main.activity_list.recycleView
import kotlinx.android.synthetic.main.activity_list.refresh
import kotlinx.android.synthetic.main.activity_list.toolbar

/**
 * Created by joe on 2018/11/20.
 * Email: lovejjfg@gmail.com
 */
abstract class BaseActivity<T> : AppCompatActivity() {
    protected open lateinit var adapter: PowerAdapter<T>
    protected lateinit var manager: LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        adapter = initAdapter()
        manager = initManager()
        recycleView.layoutManager = manager
        adapter.attachRecyclerView(recycleView)
        adapter.setOnItemClickListener { _, position, _ ->
            toast("点击了：$position")
        }
        refresh.setOnRefreshListener {
            startRefresh()
        }
    }

    abstract fun startRefresh()

    inline fun initToolbar(title: String, menu: Int? = null, crossinline callback: SucceedCallback) {
        toolbar?.apply {
            this.title = title
            if (menu != null) {
                this.inflateMenu(menu)
                this.setOnMenuItemClickListener {
                    callback.invoke(it)
                }
            }
        }
    }

    abstract fun initManager(): LayoutManager

    abstract fun initAdapter(): PowerAdapter<T>
}

typealias SucceedCallback = ((menu: MenuItem) -> Boolean)
