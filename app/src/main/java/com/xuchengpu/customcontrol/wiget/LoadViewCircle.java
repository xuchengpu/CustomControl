package com.xuchengpu.customcontrol.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/5/7 16:49.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class LoadViewCircle extends RelativeLayout {

    private CircleView leftView, middleView, rightView;
    private int duration = 300;//动画执行的时间
    private long distance = 25;//圆球移动的距离
    private AnimatorSet innerSet;
    private AnimatorSet expandSet;

    public LoadViewCircle(Context context) {
        this(context, null);
    }

    public LoadViewCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadViewCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        leftView = getCircleView(context);
        leftView.changeColor(Color.RED);
        middleView = getCircleView(context);
        middleView.changeColor(ContextCompat.getColor(context, R.color.VCheck_blue));
        rightView = getCircleView(context);
        rightView.changeColor(ContextCompat.getColor(context, R.color.VCheck_green));

        addView(leftView);
        addView(rightView);
        addView(middleView);

        //执行伸展动画
        post(new Runnable() {
            @Override
            public void run() {
                executeExpandAnimator();
            }
        });

    }

    /**
     * 伸展动画
     */
    private void executeExpandAnimator() {
        if(expandSet==null) {
            //向左的动画
            ObjectAnimator leftAnimator = ObjectAnimator.ofFloat(leftView, "translationX", 0, dp2px((int) -distance));
            //向右的动画
            ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(rightView, "translationX", 0, dp2px((int) distance));

            expandSet = new AnimatorSet();
            expandSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    executeInnerAnimator();
                }
            });
            expandSet.setDuration(duration);
            expandSet.playTogether(leftAnimator, rightAnimator);
        }
        expandSet.start();

    }

    /**
     * 收缩动画
     */
    private void executeInnerAnimator() {

        if (innerSet == null) {
            //向左的动画
            ObjectAnimator leftAnimator = ObjectAnimator.ofFloat(leftView, "translationX", dp2px((int) -distance), 0);
            //向右的动画
            ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(rightView, "translationX", dp2px((int) distance), 0);
            innerSet = new AnimatorSet();
            innerSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    executeExpandAnimator();
                    //改变颜色
                    exchangeColor();

                }
            });
            innerSet.setDuration(duration);
            innerSet.playTogether(leftAnimator, rightAnimator);
        }
        innerSet.start();
    }

    /**
     * 几个圆交换颜色
     */
    private void exchangeColor() {
        int leftViewColor = leftView.getColor();
        int middleViewColor = middleView.getColor();
        int rightViewColor = rightView.getColor();
        leftView.changeColor(middleViewColor);
        middleView.changeColor(rightViewColor);
        rightView.changeColor(leftViewColor);
    }

    private CircleView getCircleView(Context context) {
        CircleView circleView = new CircleView(context);
        LayoutParams params = new LayoutParams(dp2px(10), dp2px(10));
        params.addRule(CENTER_IN_PARENT);//设置居中
        circleView.setLayoutParams(params);
        return circleView;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        innerSet.cancel();
        expandSet.cancel();
    }
}
