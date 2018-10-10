package com.lovejjfg.swiperefreshrecycleview

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet

/**
 * Created by joe on 2018/9/28.
 * Email: lovejjfg@gmail.com
 */
class AppbarLayoutBehavior(context: Context, attrs: AttributeSet) :
    AppBarLayout.Behavior(context, attrs) {

    init {
        setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
    }
}
