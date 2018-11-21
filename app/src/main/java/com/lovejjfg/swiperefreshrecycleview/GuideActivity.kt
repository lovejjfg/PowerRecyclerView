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
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.lovejjfg.swiperefreshrecycleview.activity.CatsActivity
import kotlinx.android.synthetic.main.activity_guide.btCats
import kotlinx.android.synthetic.main.activity_guide.bt_choice
import kotlinx.android.synthetic.main.activity_guide.bt_normal

class GuideActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        bt_choice.setOnClickListener(this)
        btCats.setOnClickListener(this)
        bt_normal.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val intent: Intent? = when (v.id) {
            R.id.bt_normal -> Intent(this, NormalActivity::class.java)
            R.id.bt_choice -> Intent(this, SecondActivity::class.java)
            R.id.btCats -> Intent(this, CatsActivity::class.java)
            else -> Intent(this, CatsActivity::class.java)
        }
        startActivity(intent)
    }
}
