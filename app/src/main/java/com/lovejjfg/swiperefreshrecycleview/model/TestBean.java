package com.lovejjfg.swiperefreshrecycleview.model;

import com.lovejjfg.powerrecycle.model.SelectBean;

/**
 * Created by Joe on 2016/11/17.
 * Email lovejjfg@gmail.com
 */

public class TestBean extends SelectBean {
    private String name;

    public TestBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
