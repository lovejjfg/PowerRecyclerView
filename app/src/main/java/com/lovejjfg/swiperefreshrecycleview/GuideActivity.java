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

package com.lovejjfg.swiperefreshrecycleview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_choice, R.id.bt_customer, R.id.bt_drag, R.id.bt_grid, R.id.bt_normal, R.id.bt_refresh, R.id.bt_product})
    void click(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_normal:
                intent = new Intent(this, NormalActivity.class);
                break;
            case R.id.bt_customer:
                intent = new Intent(this, SecondActivity.class);
                break;
            case R.id.bt_drag:
                intent = new Intent(this, PickActivity.class);
                break;
            case R.id.bt_grid:
                intent = new Intent(this, SecondActivity.class);
                break;
            case R.id.bt_refresh:
                intent = new Intent(this, SecondActivity.class);
                break;
            case R.id.bt_choice:
                intent = new Intent(this, SecondActivity.class);
                break;
            case R.id.bt_product:
                intent = new Intent(this, ProductList.class);
                break;
            default:
                intent = new Intent(this, NormalActivity.class);
                break;
        }
        startActivity(intent);
    }

}
