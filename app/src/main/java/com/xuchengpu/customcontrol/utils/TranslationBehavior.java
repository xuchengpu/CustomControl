package com.xuchengpu.customcontrol.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/4/12 15:12.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class TranslationBehavior extends FloatingActionButton.Behavior {
    private boolean isShow = true;
    private View tabLayout;

    /**
     * 这个构造器必须要有 ，CoordinatorLayout就是根据这个构造器通过反射来创建实例的
     * @param context
     * @param attrs
     */
    public TranslationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 根据CoordinatorLayout源码可知，此方法在TranslationBehavior实例一被创建就会被调用
     * @param lp
     */
    @Override
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams lp) {
        super.onAttachedToLayoutParams(lp);
    }

    /**
     * 当coordinatorLayout 的子View试图开始嵌套滑动的时候被调用。当返回值为true的时候表明
     * coordinatorLayout 充当nested scroll parent 处理这次滑动，需要注意的是只有当返回值为true
     * 的时候，Behavior 才能收到后面的一些nested scroll 事件回调（如：onNestedPreScroll、onNestedScroll等）
     * 这个方法有个重要的参数nestedScrollAxes，表明处理的滑动的方向。
     *
     * @param coordinatorLayout 和Behavior 绑定的View的父CoordinatorLayout
     * @param child             和Behavior 绑定的View
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes              嵌套滑动 应用的滑动方向，看 {@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
     *                          {@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        // nestedScrollAxes 滑动关联的轴，我们只关心垂直的滑动
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }


    /**
     * 进行嵌套滚动时被调用
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed        已经消费的x方向的距离
     * @param dyConsumed
     * @param dxUnconsumed      x 方向剩下的滚动距离
     * @param dyUnconsumed
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        //根据滑动的方向指向动画
//        Log.e("TAG", "dyConsumed==" + dyConsumed);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (dyConsumed > 0) {
            //手指往上滑动
            if (!isShow) {//影藏-->展示
                child.animate().translationY(0).setDuration(400).start();
                tabLayout.animate().translationY(0).setDuration(400).start();
                isShow = true;
            }
        } else {
            if (isShow) {//展示-->影藏
                child.animate().translationY(params.bottomMargin + child.getMeasuredHeight()).setDuration(400).start();
                tabLayout.animate().translationY(params.bottomMargin + tabLayout.getMeasuredHeight()).setDuration(400).start();
                isShow = false;
            }

        }
    }

    /**摆放子view的时候使用
     * @param parent
     * @param child
     * @param layoutDirection
     * @return
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        tabLayout = parent.findViewById(R.id.bottom_tab_layout);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

}
