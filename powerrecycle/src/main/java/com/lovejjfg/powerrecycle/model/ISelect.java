package com.lovejjfg.powerrecycle.model;

/**
 * Created by Joe on 2016/11/17.
 * Email lovejjfg@gmail.com
 */

public interface ISelect {
    int SingleMode = 1;
    int MultipleMode = 2;

    boolean isSelected();

    void setSelected(boolean selected);

}
