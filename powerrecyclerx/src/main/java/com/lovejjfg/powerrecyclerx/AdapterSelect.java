/*
 * Copyright (c) 2018.  Joe
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

package com.lovejjfg.powerrecyclerx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.lovejjfg.powerrecyclerx.model.ISelect;
import java.util.Set;

/**
 * Created by Joe on 2018-11-11
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings("unused")
public interface AdapterSelect<S extends ISelect> {
    /**
     * return the select items in Set.
     *
     * @return the selected items.
     */
    @NonNull
    Set<S> getSelectedItems();

    /**
     * reset selected list and remove them form the list.
     */
    void deleteSelectedItems();

    /**
     * reset the selected list
     *
     * @param notify whether notify or not.
     */
    void clearSelectList(boolean notify);

    /**
     * revert the list in Select State.
     */
    void revertAllSelected();

    /**
     * you can specify the max number of {@link ISelect#MULTIPLE_MODE}
     *
     * @return the max number default is {@link Integer#MAX_VALUE}
     */
    int getMaxSelectCount();

    /**
     * callback this method when the selected count is arrived to {@link AdapterSelect#getMaxSelectCount()}
     *
     * @param count return the max count.
     */
    void onReceivedMaxSelectCount(int count);

    /**
     * current select model, see {@link ISelect#SINGLE_MODE} and {@link ISelect#MULTIPLE_MODE}
     *
     * @return true if there is in selectMode else otherwise.
     */
    boolean isSelectMode();

    /**
     * enable selectMode, you can call {@link AdapterSelect#setSelectedMode(int)} to update the default select model.
     *
     * @param isSelect true turn to default selectModel else turn to default click model.
     */
    void updateSelectMode(boolean isSelect);

    /**
     * set select mode in {@link ISelect#SINGLE_MODE} or {@link ISelect#MULTIPLE_MODE}
     *
     * @param mode {@link ISelect#SINGLE_MODE} or {@link ISelect#MULTIPLE_MODE}
     */
    void setSelectedMode(@SelectMode int mode);

    /**
     * enable selectMode when longTouch, you can call {@link AdapterSelect#setSelectedMode(int)} to update the default
     * select model.
     *
     * @param longTouchSelectModeEnable true turn to default selectModel else turn to default click model.
     */
    void longTouchSelectModeEnable(boolean longTouchSelectModeEnable);

    /**
     * if item is cancelable, you can cancel it when you clicked it again. in {@link ISelect#SINGLE_MODE}
     * There is always one that has default selected.
     *
     * @param isCancelAble true can be canceled,else otherwise.
     */
    void updateCancelAble(boolean isCancelAble);

    /**
     * NOTE: {@link ISelect#SINGLE_MODE} is not cancelable by default. so you even can not call such method
     * {@link AdapterSelect#clearSelectList(boolean)} or {@link AdapterSelect#deleteSelectedItems()} or
     * {@link AdapterSelect#revertAllSelected()} or {@link AdapterSelect#resetAll()}
     *
     * @return whether can cancel the select item or not.
     */
    boolean isCancelAble();

    /**
     * set the default selected position.this method maybe callback the method
     * {@link OnItemSelectedListener#onItemSelectChange(PowerHolder, int, boolean)} if the adapter has filled before.
     *
     * @param position selected position
     */
    void setCurrentPos(int position);

    void setCurrentPositions(@NonNull int... position);

    /**
     * select all the item, <b> NOTE: This is not work for model:{@link ISelect#SINGLE_MODE} </b>
     */
    void selectAll();

    /**
     * reset all the item
     */
    void resetAll();

    /**
     * you can reset all
     */
    void resetAll(boolean force);

    void setOnItemSelectListener(@Nullable OnItemSelectedListener<S> listener);

    /**
     * Interface definition for a callback to be invoked when
     * an item in this view has been selected.
     */
    interface OnItemSelectedListener<S> {
        /**
         * <p>Callback method to be invoked when an item in this view has been
         * selected. This callback is invoked only when the newly selected
         * position is different from the previously selected position or if
         * there was no selected item.</p>
         * <p>
         *
         * @param holder The holder within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param isSelected The state of isSelected
         */
        void onItemSelectChange(@NonNull PowerHolder<S> holder, int position, boolean isSelected);

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         */
        void onNothingSelected();
    }
}
