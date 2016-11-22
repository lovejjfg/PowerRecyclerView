/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.powerrecycle;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 */
public class SwipItemLayout extends FrameLayout {
    private static final String TAG = "SwipItemLayout";
    private static final String KEY_DEFAULT = "key_default";
    private static final String KEY_EXPAND = "key_expand";
    private static final String KEY_DISMISS_OFFSET = "key_dismissoffset";
    private static final String KEY_TOP_OFFSET = "key_topoffset";
    private static final String KEY_TOP = "key_top";
    // constants
    private static final int MIN_SETTLE_VELOCITY = 6000; // px/s

    private final int MIN_FLING_VELOCITY;
    private final int MIN_DRAG_DISTANCE = 200;

    // child views & helpers
    private View sheet;//target
    private ViewDragHelper sheetDragHelper;
//    private ViewOffsetHelper sheetOffsetHelper;

    // state
    private List<Callbacks> callbacks;
    private int startX;
    private int startY;

    public SwipItemLayout(Context context) {
        this(context, null, 0);
    }

    public SwipItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        MIN_FLING_VELOCITY = viewConfiguration.getScaledMinimumFlingVelocity();
    }


    /**
     * Callbacks for responding to interactions with the bottom sheet.
     */
    public static abstract class Callbacks {

        public void onSheetNarrowed() {

        }

        public void onSheetExpanded() {
        }

        public void onSheetPositionChanged(int sheetTop, float currentX, int dy, boolean userInteracted) {
        }
    }

    public void registerCallback(Callbacks callback) {
        if (callbacks == null) {
            callbacks = new CopyOnWriteArrayList<>();
        }
        callbacks.add(callback);
    }

    public void unregisterCallback(Callbacks callback) {
        if (callbacks != null && !callbacks.isEmpty()) {
            callbacks.remove(callback);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (sheet != null) {
            throw new UnsupportedOperationException("CurveLayout must only have 1 child view");
        }
        sheet = child;
        sheet.addOnLayoutChangeListener(sheetLayout);
        // force the sheet contents to be gravity bottom. This ain't a top sheet.
        ((LayoutParams) params).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        super.addView(child, index, params);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            sheetDragHelper.cancel();
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }
        return isDraggableViewUnder((int) ev.getX(), (int) ev.getY())
                && (sheetDragHelper.shouldInterceptTouchEvent(ev));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://有事件先拦截再说！！
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE://移动的时候
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();
                //判断四种情况：
                //3.上下互动，需要ListView来响应。
                if (sheetDragHelper.continueSettling(true)|| Math.abs(endX - startX) < (Math.abs(endY - startY))) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        sheetDragHelper.processTouchEvent(ev);
        return sheetDragHelper.getCapturedView() != null || super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (sheetDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sheet != null) {
            sheetDragHelper = ViewDragHelper.create((ViewGroup) sheet.getParent(), dragHelperCallbacks);
            sheetDragHelper.captureChildView(sheet, 1);
        }
    }

    private boolean isDraggableViewUnder(int x, int y) {
        return getVisibility() == VISIBLE && sheetDragHelper.isViewUnder(this, x, y);
    }




    private final ViewDragHelper.Callback dragHelperCallbacks = new ViewDragHelper.Callback() {


        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == sheet;
        }


        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return sheet.getWidth();
        }

        @Override
        public void onViewPositionChanged(View child, int left, int top, int dx, int dy) {
            // notify the offset helper that the sheets offsets have been changed externally
        }

        @Override
        public void onViewReleased(View releasedChild, float velocityX, float velocityY) {
            // dismiss on downward fling, otherwise settle back to expanded position
            // TODO: 2016/11/18 还原View
        }
    };

    private final OnLayoutChangeListener sheetLayout = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {


            // modal bottom sheet content should not initially be taller than the 16:9 keyline
            Log.e(TAG, "onLayoutChange: 布局变化了！！" + sheet.getTop());
        }
    };


    private void dispatchDismissCallback() {
        if (callbacks != null && !callbacks.isEmpty()) {
            for (Callbacks callback : callbacks) {
                callback.onSheetNarrowed();
            }
        }
    }


}
