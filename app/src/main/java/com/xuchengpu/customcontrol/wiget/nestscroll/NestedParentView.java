package com.xuchengpu.customcontrol.wiget.nestscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import java.util.Arrays;

/**
 * Created by 许成谱 on 2024/10/12 14:26.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class NestedParentView extends FrameLayout implements NestedScrollingParent {

    private static final String TAG = "zzy 父View";

    private float mDownX; // 手指第一次落下的x位置
    private float mDownY; // 手指第一次落下的y位置

    private NestedScrollingParentHelper mParentHelper;

    public NestedParentView(@NonNull Context context) {
        this(context,null);
    }

    public NestedParentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestedParentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mParentHelper = new NestedScrollingParentHelper(this);
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
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (x - mDownX);
                int dy = (int) (y - mDownY);

                // 对Left和Right进行偏移
                offsetLeftAndRight(dx);
                // 对Top和Bottom进行偏移
                offsetTopAndBottom(dy);
                break;
        }
        return true;
    }


    /**
     * 有嵌套滑动，询问该父View是否接受嵌套滑动
     *
     * @param child 直接子类(层级 child >= target)
     * @param target 发起嵌套滚动的视图
     * @param nestedScrollAxes
     * @return 是否接受嵌套滑动
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d(TAG,"onStartNestedScroll 收到子View嵌套滑动，返回true，跟随滑动。 " +
                " child:" + child.getClass().getSimpleName() + "  target:" + target.getClass().getSimpleName() + "  axes: " + nestedScrollAxes);
        return true;
    }

    /**
     * 响应嵌套滚动的成功声明(接受嵌套滑动)
     * onStartNestedScroll返回true，该函数被调用
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mParentHelper.onNestedScrollAccepted(child, target, axes);
        Log.d(TAG,"onNestedScrollAccepted  已经接受嵌套滑动");
    }

    /**
     * 停止嵌套滑动
     *
     * @param child 具体嵌套滑动的那个子类
     */
    @Override
    public void onStopNestedScroll(View child) {
        Log.d(TAG,"onStopNestedScroll");
        mParentHelper.onStopNestedScroll(child);
    }

    /**
     * 响应进行中的嵌套滚动(嵌套滑动的子View在滑动之后传递过来的滑动情况)
     *
     * @param target 嵌套滑动的子View
     * @param dxConsumed 水平方向target滑动的距离
     * @param dyConsumed 竖直方向target滑动的距离
     * @param dxUnconsumed 水平方向target未滑动的距离
     * @param dyUnconsumed 竖直方向target未滑动的距离
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG,"onNestedScroll");
    }

    /**
     * 在嵌套滑动的子View未滑动之前，通知准备(响应target)滑动的情况
     *
     * @param target 嵌套滑动的View
     * @param dx 水平方向target想要移动的距离
     * @param dy 竖直方向target想要移动的距离
     * @param consumed 输出结果，告诉子View当前父View消耗的距离，让子View做出相应调整
     *                 consumed[0] 水平消耗的距离
     *                 consumed[1] 垂直消耗的距离。
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        final View child = target;
        Log.d(TAG,"onNestedPreScroll 收到： dx:" + dx + "  dy:" + dy + "  consumed: " + Arrays.toString(consumed));
        if (dx > 0){
            if (child.getRight() + dx > getWidth()){
                dx = child.getRight() + dx - getWidth(); //多出来的部分
                offsetLeftAndRight(dx);
                consumed[0] += dx; //父View消耗
            }
        }else {
            if (child.getLeft() + dx < 0){
                dx += child.getLeft();
                offsetLeftAndRight(dx);
                consumed[0] += dx; //父View消耗
            }
        }

        if (dy > 0){
            if (child.getBottom() + dy > getHeight()){
                dy = child.getBottom() + dy - getHeight();
                offsetTopAndBottom(dy);
                consumed[1] += dy;
            }
        }else {
            if (child.getTop() + dy < 0){
                dy = dy + child.getTop();
                offsetTopAndBottom(dy);
                consumed[1] += dy;
            }
        }
        Log.d(TAG,"onNestedPreScroll 输出： dx:" + dx + "  dy:" + dy + "  consumed: " + Arrays.toString(consumed));
    }

    /**
     * 嵌套滑动的子View在fling之后报告过来的fling情况
     *
     * @param target
     * @param velocityX
     * @param velocityY
     * @param consumed
     * @return 父View是否消耗了fling
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.d(TAG,"onNestedFling");
        return false;
    }

    /**
     *
     * 在嵌套滑动的子View未fling之前告诉过来的准备fling的情况
     * @param target 嵌套滑动的View
     * @param velocityX
     * @param velocityY
     * @return 父View是否消耗fling
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.d(TAG,"onNestedPreFling");
        return false;
    }

    /**
     * 获得滚动方向
     *
     * @return 返回滚动轴
     * @see ViewCompat#SCROLL_AXIS_HORIZONTAL
     * @see ViewCompat#SCROLL_AXIS_VERTICAL
     * @see ViewCompat#SCROLL_AXIS_NONE
     */
    @Override
    public int getNestedScrollAxes() {
        return super.getNestedScrollAxes();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e(TAG,"onScrollChanged");
    }
}


