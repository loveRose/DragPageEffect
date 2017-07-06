package com.app.lvyerose.drag.dragpageeffect.view;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/************************************************************************
 *                    .::::.                                            *
 *                  .::::::::.                                          *
 *                 :::::::::::  FUCK YOU                                *
 *             ..:::::::::::'                                           *
 *           '::::::::::::'                                             *
 *             .::::::::::                                              *
 *        '::::::::::::::..                                             *
 *             ..::::::::::::.                                          *
 *           ``::::::::::::::::                                         *
 *            ::::``:::::::::'        .:::.                             *
 *           ::::'   ':::::'       .::::::::.                           *
 *         .::::'      ::::     .:::::::'::::.                          *
 *        .:::'       :::::  .:::::::::' ':::::.                        *
 *       .::'        :::::.:::::::::'      ':::::.                      *
 *      .::'         ::::::::::::::'         ``::::.                    *
 *  ...:::           ::::::::::::'              ``::.                   *
 * ```` ':.          ':::::::::'                  ::::..                *
 *                    '.:::::'                    ':'````..             *
 *              ━━━━━━━━━━━━━━━━━━━━━                                   *
 ************************************************************************
 *
 * 项目名称：DragLayoutMulti
 * 类描述： 三个拖拽切换的ViewGroup  如京东淘宝商品详情页上拉加载详情等
 * 创建人：lvyerose
 * 邮箱：lvyerose@163.com
 * 创建时间：17/6/28
 *
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 *  ......
 */
public class DragMultiLayout extends ViewGroup {

    /* 拖拽工具类 */
    private final ViewDragHelper mDragHelper;
    private GestureDetectorCompat gestureDetector;

    /* 上下两个frameLayout，在Activity中注入fragment */
    private View frameViewHead, frameViewBetween, frameViewBottom;
    private int viewHeadHeight;
    private int viewBetweenHeight;
    private static final int VEL_THRESHOLD = 100; // 滑动速度的阈值，超过这个绝对值认为是上下
    //    private static final int DISTANCE_THRESHOLD = 100; // 单位是像素，当上下滑动速度不够时，通过这个阈值来判定是应该粘到顶部还是底部
    private int downTopHead; // 手指按下的时候，frameViewHead 的getTop值
    private int downTopBetween; // 手指按下的时候，frameViewBetween 的getTop值
    private ShowNextPageNotifier nextPageListener; // 手指松开是否加载下一页的notifier

    private int touchIndex = 0;
    private int intoIndex = 0;
    private float betweenScale = 1.0f / 4; //  最后一屏额外显示上一屏的比例

    public DragMultiLayout(Context context) {
        this(context, null);
    }

    public DragMultiLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragMultiLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper
                .create(this, 10f, new DragHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
        gestureDetector = new GestureDetectorCompat(context,
                new YScrollDetector());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 跟findviewbyId一样，初始化上下两个view
        frameViewHead = getChildAt(0);
        frameViewBetween = getChildAt(1);
        frameViewBottom = getChildAt(2);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
                                float dy) {
            // 垂直滑动时dy>dx，才被认定是上下拖动
            return Math.abs(dy) > Math.abs(dx);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 这是拖拽效果的主要逻辑
     */
    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            int childIndex = 0;
            if (changedView == frameViewBetween) {
                childIndex = 1;
            } else if (changedView == frameViewBottom) {
                childIndex = 2;
            }
            // 一个view位置改变，另一个view的位置要跟进
            onViewPosChanged(childIndex, top);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // 两个子View都需要跟踪，返回true
            return true;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            // 这个用来控制拖拽过程中松手后，自动滑行的速度，暂时给一个随意的数值
            return 1;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // 滑动松开后，需要向上或者乡下粘到特定的位置
            animTopOrBottom(releasedChild, yvel);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int finalTop = top;
            if (child == frameViewHead) {
                // 拖动的时第一个view
                if (top > 0) {
                    // 不让第一个view往下拖，因为顶部会白板
                    finalTop = 0;
                }
            } else if (child == frameViewBetween) {
                //不做任何处理
            } else if (child == frameViewBottom) {
                // 拖动的时第三个view
                if (top < 0) {
                    // 不让第二个view网上拖，因为底部会白板
                    finalTop = 0;
                }
            }

            // finalTop代表的是理论上应该拖动到的位置。此处计算拖动的距离除以一个参数(3)，是让滑动的速度变慢。数值越大，滑动的越慢
            int result = child.getTop() + (finalTop - child.getTop()) / 3;
            return result;
        }
    }

    /**
     * 滑动时view位置改变协调处理
     *
     * @param viewIndex 滑动view的index(1或2)
     * @param posTop    滑动View的top位置
     */
    private void onViewPosChanged(int viewIndex, int posTop) {
        touchIndex = viewIndex;
        if (viewIndex == 0) {
            int offsetTopBottom = viewHeadHeight + frameViewHead.getTop()
                    - frameViewBetween.getTop();
            frameViewBetween.offsetTopAndBottom(offsetTopBottom);
        } else if (viewIndex == 1) {
            if (posTop > 0) {
                int offsetTopBottom = frameViewBetween.getTop() - viewHeadHeight
                        - frameViewHead.getTop();
                frameViewHead.offsetTopAndBottom(offsetTopBottom);
            } else if (posTop <= 0) {
                int offsetTopBottom = viewBetweenHeight + frameViewBetween.getTop()
                        - frameViewBottom.getTop();
                frameViewBottom.offsetTopAndBottom(offsetTopBottom);
            }
        } else if (viewIndex == 2) {
            int offsetTopBottom = frameViewBottom.getTop() - viewBetweenHeight
                    - frameViewBetween.getTop();
            frameViewBetween.offsetTopAndBottom(offsetTopBottom);
        }
        // 有的时候会默认白板，这个很恶心。后面有时间再优化
        invalidate();
    }

    private void animTopOrBottom(View releasedChild, float yVel) {
        int finalTop = 0; // 默认是粘到最顶端
        if (releasedChild == frameViewHead) {
            // 拖动第一个view松手
            if (yVel < -VEL_THRESHOLD
//                    || (downTopHead == 0 && frameViewHead.getTop() < -DISTANCE_THRESHOLD)
                    ) {
                // 向上的速度足够大，就滑动到顶端
                // 向上滑动的距离超过某个阈值，就滑动到顶端
                finalTop = -viewHeadHeight;
                intoIndex = 1;
                // 下一页可以初始化了
                if (null != nextPageListener) {
                    nextPageListener.onDragNext(1);
                }
            } else {
                intoIndex = 0;
            }
        } else if (releasedChild == frameViewBetween) {
            if (yVel <= 0) {
                //滑动开启底部View
                if (yVel < -VEL_THRESHOLD
//                        || (downTopBetween == 0 && frameViewBetween.getTop() < -DISTANCE_THRESHOLD)
                        ) {
                    // 向上的速度足够大，就滑动到顶端
                    // 向上滑动的距离超过某个阈值，就滑动到顶端
                    finalTop = (int) -(viewBetweenHeight - viewBetweenHeight * betweenScale);
                    intoIndex = 2;
                    // 下一页可以初始化了
                    if (null != nextPageListener) {
                        nextPageListener.onDragNext(2);
                    }
                } else {
                    intoIndex = 1;
                }
            } else if (yVel > 0) {
                //滑动开启顶部View
                if (yVel > VEL_THRESHOLD && frameViewBetween.getTop() > 0
//                        || (downTopHead == -viewBetweenHeight && releasedChild.getTop() > DISTANCE_THRESHOLD)
                        ) {
                    intoIndex = 0;
                    finalTop = viewBetweenHeight;
                } else {
                    intoIndex = 1;
                }
            }
        } else if (releasedChild == frameViewBottom) {
            // 拖动第最后一个view松手 只能返回中间的View
            if (yVel > VEL_THRESHOLD
//                    || (downTopBetween == -viewBetweenHeight && releasedChild.getTop() > DISTANCE_THRESHOLD)
                    ) {
                finalTop = viewBetweenHeight;
                intoIndex = 1;
            } else {
                //没有滑过足够位置时回到2/3位置
                intoIndex = 2;
                finalTop = (int) (viewBetweenHeight * betweenScale);
            }
        }

        if (mDragHelper.smoothSlideViewTo(releasedChild, 0, finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    //是否拦截当前页面的事件
    private boolean isInterceptOnTouchEvent() {
        Log.e("LOGS", touchIndex + "--||--" + intoIndex);
        if (touchIndex == 0 || intoIndex == 0) {
            //第一个滑动 只有往上滑出第二页的情况
            //满足下面条件则说明该动作还在滑动中
            if (frameViewHead.getBottom() > 0 && frameViewHead.getTop() < 0) {
                // view粘到顶部或底部，正在动画中的时候，不处理touch事件
                return true;
            }
        } else if (touchIndex == 1 && intoIndex == 1) {
            if (frameViewBetween.getTop() != 0) {
                return true;
            }
        } else if (touchIndex == 1 && intoIndex == 0) {
            if (frameViewBetween.getTop() != 0) {
                return true;
            }
        } else if (touchIndex == 2 && intoIndex == 1) {
            if (frameViewBetween.getTop() != 0) {
                return true;
            }
        } else if (touchIndex == 1 && intoIndex == 2) {
//            if (frameViewBottom.getTop() != ((int) (viewBetweenHeight * betweenScale))) {
//                return true;
//            }
        }
        return false;

    }

    /* touch事件的拦截与处理都交给mDraghelper来处理 */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isInterceptOnTouchEvent()) {
            //先判断当前是否要拦截不处理这个再次触摸的事件，如果要拦截那么直接返回false就行了
            return false;
        }

        boolean yScroll = gestureDetector.onTouchEvent(ev);
        boolean shouldIntercept = false;
        try {
            shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
        }
        int action = ev.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            // action_down时就让mDragHelper开始工作，否则有时候导致异常 他大爷的
            mDragHelper.processTouchEvent(ev);
            downTopHead = frameViewHead.getTop();
            downTopBetween = frameViewBetween.getTop();
        }
        return shouldIntercept && yScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // 统一交给mDragHelper处理，由DragHelperCallback实现拖动效果
        mDragHelper.processTouchEvent(e); // 该行代码可能会抛异常，正式发布时请将这行代码加上try catch
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 只在初始化的时候调用
        // 一些参数作为全局变量保存起来

        if (frameViewHead.getTop() == 0) {
            // 只在初始化的时候调用
            // 一些参数作为全局变量保存起来
            frameViewHead.layout(l, 0, r, b - t);
            frameViewBetween.layout(l, 0, r, b - t);
            frameViewBottom.layout(l, 0, r, b - t);

            viewHeadHeight = frameViewHead.getMeasuredHeight();
            viewBetweenHeight = frameViewBetween.getMeasuredHeight();
            frameViewBetween.offsetTopAndBottom(viewHeadHeight);
            frameViewBottom.offsetTopAndBottom(viewBetweenHeight);
        } else {
            // 如果已被初始化，这次onLayout只需要将之前的状态存入即可
            frameViewHead.layout(l, frameViewHead.getTop(), r, frameViewHead.getBottom());
            frameViewBetween.layout(l, frameViewBetween.getTop(), r, frameViewBetween.getBottom());
            frameViewBottom.layout(l, frameViewBottom.getTop(), r, frameViewBottom.getBottom());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    /**
     * 这是View的方法，该方法不支持android低版本（2.2、2.3）的操作系统，所以手动复制过来以免强制退出
     */
    public static int resolveSizeAndState(int size, int measureSpec,
                                          int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }

    public void setNextPageListener(ShowNextPageNotifier nextPageListener) {
        this.nextPageListener = nextPageListener;
    }

    public interface ShowNextPageNotifier {
        void onDragNext(int nextIndex);
    }
}
