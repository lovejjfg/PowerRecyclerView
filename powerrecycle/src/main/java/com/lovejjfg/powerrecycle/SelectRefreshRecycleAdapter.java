package com.lovejjfg.powerrecycle;

import android.util.Log;
import android.view.View;

import com.lovejjfg.powerrecycle.model.SelectBean;

/**
 * Created by Joe on 2016-03-11
 * Email: lovejjfg@gmail.com
 */
public abstract class SelectRefreshRecycleAdapter<T extends SelectBean> extends RefreshRecycleAdapter<T> implements AdapterLoader<T> {

    private static final int SingleMode = 1;
    private static final int MultipleMode = 2;
    private static int currentMode = 1;
    //    private TestBean pre;
    private int prePos;
    public static boolean isSelectMode;

    public void updateSelectMode(boolean isSelect) {
        if (isSelectMode != isSelect) {
            isSelectMode = isSelect;
            resetData();
            notifyDataSetChanged();
        }
    }

    private void resetData() {
        for (SelectBean bean : list) {
            bean.setSelected(false);
        }
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    @Override
    public void performClick(final View itemView, final int position) {
        final SelectBean testBean = list.get(position);

        if (isSelectMode) {
            // TODO: 2016/11/17 不可见为什么响应点击事件了！！
            Log.e("TAG", "onViewHolderBind: " + position + "点击了！！");
            testBean.setSelected(!testBean.isSelected());
            if (currentMode == SingleMode && position != prePos && testBean.isSelected()) {
                list.get(prePos).setSelected(false);
                notifyItemChanged(prePos);
            }
            notifyItemRangeChanged(position, 1);
            prePos = position;
        } else {
            if (listener != null) {
                listener.onItemClick(itemView, position);
            }
        }
    }

    @Override
    public boolean performLongClick(View itemView, int position) {
        final SelectBean testBean = list.get(position);
        updateSelectMode(true);
        testBean.setSelected(!testBean.isSelected());
        prePos = position;
        return true;
    }
}
