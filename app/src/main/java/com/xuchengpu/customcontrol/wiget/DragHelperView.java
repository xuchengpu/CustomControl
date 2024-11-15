package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * Created by 许成谱 on 2018/4/10 18:21.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class DragHelperView extends FrameLayout {
    private ViewDragHelper dragHelper;//系统提供给我们用于拖动子view的工具类
    private View dragListview;//上层的子孩子实例
    private View menuView;//下层的菜单栏实例
    private int menuHeight;//菜单栏高度
    private  boolean menuIsOpen=false;//菜单栏是否打开

    public DragHelperView(@NonNull Context context) {
        this(context, null);
    }

    public DragHelperView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragHelperView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper = ViewDragHelper.create(this, new MyCalllBack());//只能通过建造者模式来创建
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("DragHelperView 只能包含两个子view");
        }
        menuView = getChildAt(0);//拿到菜单栏
        dragListview = getChildAt(1);//拿到listview

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            menuHeight = menuView.getMeasuredHeight();//必须在测量之后，即super.onMeasure()之后、onlayout()、onSizeChanged()中等都可以获取控件的高度
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);//将事件传递给ViewDragHelper，注意要是事件传递不完整，就会报错
        return true;//如果不return true,则后续的move和up将不会再传递给它了，dragHelper就不能发挥作用了
    }


    //上部的孩子换成listview后，拖动效果失效？事件被listview给消费掉了
    //Ignoring pointerId=0 because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.
    //报上面这个错是因为拦截后move事件传递给了dragHelper,但down事件还没有传递过去因此需要加上第A步
    private float downY;
    private float moveY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                dragHelper.processTouchEvent(ev);//第A步
                break;
            case MotionEvent.ACTION_MOVE:
                moveY=ev.getY();
                if(moveY-downY>0&&!menuIsOpen&&!canChildScrollUp(dragListview)) {//向下滑动且位于顶部且listview的item位于最顶端
                    return true;//拦截后事件传递给本层的TouchEvent，即传递给了draghelper.
                }
                if(menuIsOpen&&moveY-downY<0) {//菜单栏是打开状态且是向上滑动
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    class MyCalllBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //true:所有子view都可拖动
            return dragListview == child;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //形参由ViewDragHelper回调得到
            //返回值用于设置竖直方向拖动的距离

            if (top < 0) {
                top = 0;
            }
            if (top > menuHeight) {
                top = menuHeight;
            }
            return top;
        }

        //        @Override
//        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            //返回值设置水平方向的距离
//            return left;
//        }
        //手指松开的时候两者选其一，要么打开要么关闭
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int top = dragListview.getTop();
            if (top < menuHeight / 2) {
                dragHelper.settleCapturedViewAt(0, 0);
                menuIsOpen=false;
            } else {
                dragHelper.settleCapturedViewAt(0, menuHeight);
                menuIsOpen=true;
            }
            invalidate();//实现回弹效果必须要加上的
        }
    }

    //响应滚动，让dragHelper回弹必须重写的
    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    //从SwipeRefreshLayout中扒取而来，学会利用系统以后的方法工具,判断listview时候滑到顶
    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp(View view) {

        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(view, -1) || view.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(view, -1);
        }
    }
}
