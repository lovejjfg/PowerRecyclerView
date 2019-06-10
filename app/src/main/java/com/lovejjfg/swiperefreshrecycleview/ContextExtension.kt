package com.lovejjfg.swiperefreshrecycleview

import android.content.Context
import android.widget.Toast

/**
 * Created by joe on 2019-06-10.
 * Email: lovejjfg@gmail.com
 */

fun Context.toast(msg: String, type: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, type).show()
}
