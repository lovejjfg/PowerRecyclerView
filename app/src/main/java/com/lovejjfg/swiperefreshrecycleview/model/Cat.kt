package com.lovejjfg.swiperefreshrecycleview.model

import com.lovejjfg.powerrecyclerx.model.ISelect

data class Cat(
    val src: Int,
    var name: String,
    var selectedd: Boolean = false
) : ISelect {
    override fun isSelected(): Boolean {
        return selectedd
    }

    override fun setSelected(selected: Boolean) {
        this.selectedd = selected
    }
}
