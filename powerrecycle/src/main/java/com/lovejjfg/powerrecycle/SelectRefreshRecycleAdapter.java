package com.lovejjfg.powerrecycle;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.lovejjfg.powerrecycle.model.SelectBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;

/**
 * Created by Joe on 2016-03-11
 * Email: lovejjfg@gmail.com
 */
public abstract class SelectRefreshRecycleAdapter<T extends SelectBean> extends RefreshRecycleAdapter<T> {


    @IntDef(flag = true, value = {
            SingleMode,
            MultipleMode
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ChoiceMode {
    }

    private int currentMode = 1;
    //    private TestBean pre;
    private int prePos;
    public static boolean isSelectMode;

    public HashSet<T> getSelectedBeans() {
        return selectedBeans;
    }

    private HashSet<T> selectedBeans = new HashSet<>();


//    @IntDef(flag = true, value = {
//            SingleMode, MultipleMode
//    })

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


    public void setMultipleMode(@ChoiceMode int mode) {
        currentMode = mode;
    }

    @Override
    public void performClick(final View itemView, final int position) {
        final T testBean = list.get(position);

        if (isSelectMode) {
            // TODO: 2016/11/17 不可见为什么响应点击事件了！！
            Log.e("TAG", "onViewHolderBind: " + position + "点击了！！");
            boolean selected = !testBean.isSelected();
            testBean.setSelected(selected);
            dispatchSelected(itemView, position, testBean, selected);
            if (currentMode == SingleMode && position != prePos && testBean.isSelected()) {
                list.get(prePos).setSelected(false);
                dispatchSelected(itemView, prePos, testBean, false);
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

    private void dispatchSelected(View itemView, int position, T testBean, boolean isSelected) {
        if (isSelected) {
            selectedBeans.add(testBean);
        } else {
            selectedBeans.remove(testBean);
            if (selectedListener != null && selectedBeans.isEmpty()) {
                selectedListener.onNothingSelected();
            }
        }
        if (selectedListener != null) {
            selectedListener.onItemSelected(itemView, position, isSelected);
        }
    }

    @Override
    public boolean performLongClick(View itemView, int position) {
        final T testBean = list.get(position);
        updateSelectMode(true);
        testBean.setSelected(true);
        dispatchSelected(itemView, position, testBean, true);
        prePos = position;
        return true;
    }

    @Nullable
    OnItemSelectedListener selectedListener;

    public void setOnItemSelectListener(OnItemSelectedListener listener) {
        this.selectedListener = listener;
    }
}
