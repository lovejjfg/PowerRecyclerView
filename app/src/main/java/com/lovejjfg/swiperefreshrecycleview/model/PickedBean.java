package com.lovejjfg.swiperefreshrecycleview.model;

import com.lovejjfg.powerrecycle.model.ISelect;

/**
 * Created by Joe on 2016/10/14.
 * Email lovejjfg@gmail.com
 */

public class PickedBean implements ISelect {

    public boolean isChecked;
    public String title;

    public PickedBean(String title) {
        this.title = title;
    }

    @Override

    public boolean isSelected() {
        return isChecked;
    }

    @Override
    public void setSelected(boolean selected) {
        isChecked = selected;
    }
}
