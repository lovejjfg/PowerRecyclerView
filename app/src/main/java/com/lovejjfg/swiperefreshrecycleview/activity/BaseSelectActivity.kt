package com.lovejjfg.swiperefreshrecycleview.activity

import android.os.Bundle
import android.util.Log
import com.lovejjfg.powerrecyclerx.AdapterSelect.OnItemSelectedListener
import com.lovejjfg.powerrecyclerx.PowerHolder
import com.lovejjfg.powerrecyclerx.SelectPowerAdapter
import com.lovejjfg.powerrecyclerx.model.ISelect
import com.lovejjfg.swiperefreshrecycleview.toast

/**
 * Created by joe on 2018/11/20.
 * Email: lovejjfg@gmail.com
 */
abstract class BaseSelectActivity<T : ISelect> : BaseActivity<T>() {
    protected lateinit var selectAdapter: SelectPowerAdapter<T>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectAdapter = adapter as SelectPowerAdapter<T>
        selectAdapter.setOnItemSelectListener(object : OnItemSelectedListener<T> {
            override fun onNothingSelected() {
                Log.e(this@BaseSelectActivity::class.java.simpleName, "onNothingSelected:...")
//                toast("一个都没有")
            }

            override fun onItemSelectChange(holder: PowerHolder<T>, position: Int, isSelected: Boolean) {
                Log.e(
                    this@BaseSelectActivity::class.java.simpleName,
                    "onItemSelectChange:$position;isSelected:$isSelected"
                )
//                toast("选中：$position,total：${selectAdapter.selectedItems.size}")
            }
        })
    }

    override fun onBackPressed() {
        if (selectAdapter.isSelectMode) {
//            selectAdapter.notifyItemChanged(0, "xxxx0")
//            selectAdapter.notifyItemChanged(1, "xxxx1")
//            selectAdapter.clearSelectList(true)
            selectAdapter.updateSelectMode(false)
            toast("已推出选择模式")
        } else {
            super.onBackPressed()
        }
    }
}
