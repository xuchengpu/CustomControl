package com.xuchengpu.customcontrol.wiget.nestscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import java.util.Arrays;

/**
 * Created by 许成谱 on 2024/10/12 14:27.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class NestedChildView extends AppCompatTextView implements NestedScrollingChild {

    private static final String TAG = "NestedChildView";

    private float mDownX; // 手指第一次落下的x位置
    private float mDownY; // 手指第一次落下的y位置

    /**
     * 接受父View消耗的值
     */
    private int[] consumed = new int[2]; // 消耗的距离
    private int[] offsetInWindow = new int[2]; // 窗口偏移

    private NestedScrollingChildHelper mChildHelper;

    public NestedChildView(Context context) {
        this(context,null);
    }

    public NestedChildView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestedChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        // 开启嵌套滑动
        setNestedScrollingEnabled(true);
        setText("移动我");
        setGravity(Gravity.CENTER);
        setTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                //开始滑动时，通知父View
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL
                        | ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (x - mDownX);
                int dy = (int) (y - mDownY);

                //分发触屏事件给父类处理
                if (dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)){
                    //减掉父类消耗的距离
                    dx -= consumed[0];
                    dy -= consumed[1];
                }
                // 对Left和Right进行偏移
                offsetLeftAndRight(dx);
                // 对Top和Bottom进行偏移
                offsetTopAndBottom(dy);
                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;
        }
        return true;
    }

    /**
     * 设置是否允许嵌套滑动
     *
     * @param enabled
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.i(TAG,"设置是否允许嵌套欢动：" + enabled);
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    /**
     * 是否允许嵌套滑动
     *
     * @return
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        Log.i(TAG,"获得是否允许嵌套欢动：" + mChildHelper.isNestedScrollingEnabled());
        return mChildHelper.isNestedScrollingEnabled();
    }

    /**
     * 通知开始嵌套滑动流程，调用该函数的时候会去找嵌套滑动的父控件
     * 如果找到了父控件并且父控件说可以滑动就返回true，否则返回false
     * (一般在ACTION_DOWN 事件类型中调用)
     *
     * @param axes 支持嵌套滑动轴：水平方向、垂直方向、或者不指定方向
     * @return
     */
    @Override
    public boolean startNestedScroll(int axes) {
        Log.i(TAG,"startNestedScroll 开始滚动,通知父View");
        return mChildHelper.startNestedScroll(axes);
    }

    /**
     * 停止嵌套滑动(一般在 ACTION_UP 中调用)
     */
    @Override
    public void stopNestedScroll() {
        Log.i(TAG,"stopNestedScroll 停止嵌套滚动");
        mChildHelper.stopNestedScroll();
    }

    /**
     * 是否有嵌套滑动对应的父控件
     *
     * @return
     */
    @Override
    public boolean hasNestedScrollingParent() {
        Log.i(TAG,"是否有嵌套滚动的父View：" + mChildHelper.hasNestedScrollingParent());
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        Log.i(TAG,"分发嵌套滑动 dispatchNestedScroll  dxC:" + dxConsumed + "  dyC:" + dyConsumed + "  dxU:" + dxUnconsumed + "  dyU:" + dyUnconsumed + "  oI:" + offsetInWindow);
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     * 在嵌套滑动的子View滑动之前，告诉父View滑动额距离，让父View做相应的处理
     *
     * @param dx 告诉父View水平方向需要滑动的距离
     * @param dy 告诉父View垂直方向需要滑动的距离
     * @param consumed 如果不为NULL，则告诉子View父View的滑动情况:
     *                 consumed[0] 父View告诉子View水平方向滑动的距离(dx)
     *                 consumed[1] 父View告诉子View垂直方向滑动的距离(dy)
     * @param offsetInWindow 可选，length=2的数组，如果父View滑动导致子View的窗口发生了变化(子View的位置发生变化)
     *                       该参数返回x(offsetInWindow[0]),y(offsetInWindow[1])方向的变化
     *                       如果你记录了手指最后的位置，需要根据参数offsetInWindow计算偏移量，
     *                       才能保证下一次的touch事件的计算是正确的。
     * @return true 父View滑动了；false 父View没有滑动。
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        Log.i(TAG,"准备分发 dispatchNestedPreScroll dx:" + dx + "  dy:" + dy + "  cons:" + Arrays.toString(consumed) + "  offs:" + Arrays.toString(offsetInWindow));
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    /**
     * 在嵌套滑动的子View fling之后在调用该函数向父View汇报fling情况
     *
     * @param velocityX 水平方向速度
     * @param velocityY 垂直方向速度
     * @param consumed  true：子View fling了，false：子View没有 fling
     * @return true 父View fling了
     */
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Log.i(TAG,"分发Fling dispatchNestedFling  vX:" + velocityX + "  vY:" + velocityY + "   consumed:" + consumed);
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    /**
     * 在嵌套滑动的子View fling之前告诉父View fling情况
     *
     * @param velocityX 水平方向的速度
     * @param velocityY 垂直方向的速度
     * @return true: 父View fling了
     */
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Log.i(TAG,"准备分发Fling  dispatchNestedPreFling  vX:" + velocityX + "  vY:" + velocityY);
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mChildHelper.onDetachedFromWindow();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e(TAG,"onScrollChanged");
    }
}

