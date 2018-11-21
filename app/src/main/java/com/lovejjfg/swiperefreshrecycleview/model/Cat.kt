package com.lovejjfg.swiperefreshrecycleview.model

import com.lovejjfg.powerrecycle.model.ISelect

data class Cat(
    val src: Int,
    val name: String,
    var selectedd: Boolean = false
) : ISelect {
    override fun isSelected(): Boolean {
        return selectedd
    }

    override fun setSelected(selected: Boolean) {
        this.selectedd = selected
    }
}
