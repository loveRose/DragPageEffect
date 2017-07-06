package com.app.lvyerose.drag.dragpageeffect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 只为顶部ScrollView使用
 * 如果使用了其它的可拖拽的控件，请仿照该类实现isAtBottom方法
 */
public class BetweenViewGroup extends ScrollView {
    private static final int TOUCH_IDLE = 0;
    private static final int TOUCH_INNER_CONSUME = 1; // touch事件由ScrollView内部消费
    private static final int TOUCH_DRAG_LAYOUT = 2; // touch事件由上层的DragLayout去消费

    boolean isAtBottom = true; // 按下的时候是否在底部
    private int mTouchSlop = 4; // 判定为滑动的阈值，单位是像素
    private int scrollMode;
    private float downY;
    private float downX;

    public BetweenViewGroup(Context arg0) {
        this(arg0, null);
    }

    public BetweenViewGroup(Context arg0, AttributeSet arg1) {
        this(arg0, arg1, 0);
    }

    public BetweenViewGroup(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //子View一定要Clickable才行，否则onInterceptTouchEvent工作不按正常来
        getChildAt(0).setClickable(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getRawX();
            downY = ev.getRawY();
            isAtBottom = isAtBottom();
            scrollMode = TOUCH_IDLE;
//            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (scrollMode == TOUCH_IDLE) {
                float yOffset = downY - ev.getRawY();
                float xOffset = downX - ev.getRawY();
                float xDistance = Math.abs(xOffset);
                float yDistance = Math.abs(yOffset);

                if (xDistance > yDistance / 2 && xDistance > mTouchSlop) {
                    //横线滑动
                    return false;
                }
                if (yDistance > xDistance && yDistance > mTouchSlop && isAtBottom) {
                    scrollMode = TOUCH_DRAG_LAYOUT;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scrollMode == TOUCH_DRAG_LAYOUT) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    private boolean isAtBottom() {
        return getScrollY() == 0;
    }

}
