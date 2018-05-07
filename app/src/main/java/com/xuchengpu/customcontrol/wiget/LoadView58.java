package com.xuchengpu.customcontrol.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/5/4 13:22.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class LoadView58 extends LinearLayout {

    private Imitation58View shapeView;
    private View loadShadow;
    private float bounceDistance = 80;
    private float scaleDistance = 3;
    private long animatorDuration = 500;
    private CurrentState state = CurrentState.FALLING;

    public LoadView58(Context context) {
        this(context, null);
    }

    public LoadView58(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadView58(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.inflate(getContext(), R.layout.loadview, this);//讲另一个布局加载进来，第三个参数不为空即将目标layout添加进this里，this为目标layout的父布局
        shapeView = (Imitation58View) findViewById(R.id.shape_view);
        loadShadow = findViewById(R.id.load_shadow);

        post(new Runnable() {
            @Override
            public void run() {
                startFallAnimator();//使这个方法在onresume中执行，放在post外的话会在oncreate方法中执行
            }
        });

    }

    /**
     * 开始降落动画
     */
    private void startFallAnimator() {
        if(isStopAnimator) {
            return;
        }
        state = CurrentState.FALLING;//当前状态为坠落状态
        //属性动画
        ObjectAnimator fallAnimator = ObjectAnimator.ofFloat(shapeView, "translationY", 0, dp2px(bounceDistance));
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(loadShadow, "scaleX", 0, dp2px(scaleDistance));
        ObjectAnimator rotationAnimator = getRotationAnimator();
        //几个属性动画一起执行
        AnimatorSet set = new AnimatorSet();
        set.setDuration(animatorDuration);
        set.setInterpolator(new AccelerateInterpolator());//开始慢，末尾快
        set.playTogether(fallAnimator, scaleAnimator, rotationAnimator);
        set.addListener(new AnimatorListenerAdapter() {//设置监听，即下落动画执行完开始执行跳起动画
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //变换形状
                shapeView.change();
                //执行跳起动画
                startBounceAnimator();
            }
        });
        set.start();

    }


    /**
     * 开始弹起动画
     */
    private void startBounceAnimator() {
        state = CurrentState.BOUNCE;//当前状态为弹起状态
        //属性动画
        ObjectAnimator fallAnimator = ObjectAnimator.ofFloat(shapeView, "translationY", dp2px(bounceDistance), 0);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(loadShadow, "scaleX", dp2px(scaleDistance), 0);
        ObjectAnimator rotationAnimator = getRotationAnimator();
        //几个属性动画一起执行
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fallAnimator, scaleAnimator, rotationAnimator);
        set.setDuration(animatorDuration);
        set.setInterpolator(new DecelerateInterpolator());//开始快，末尾慢
        set.addListener(new AnimatorListenerAdapter() {//设置监听，即跳起动画执行完开始执行下降动画
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //执行下降动画
                startFallAnimator();

            }
        });
        set.start();
    }

    private float dp2px(float size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    enum CurrentState {
        FALLING, BOUNCE
    }

    /**
     * 获取旋转的动画
     *
     * @return
     */
    private ObjectAnimator getRotationAnimator() {
        ObjectAnimator rotationAnimator = null;
        Imitation58View.Shape currentShape = shapeView.getCurrentShape();
        if (state == CurrentState.FALLING) {
            switch (currentShape) {
                case Triangle:
                    rotationAnimator = ObjectAnimator.ofFloat(shapeView, "rotation", 0, -360);
                    break;
                case Rectangle:
                    rotationAnimator = ObjectAnimator.ofFloat(shapeView, "rotation", 0, 180);
                    break;
                case Circle:
                    rotationAnimator = ObjectAnimator.ofFloat(shapeView, "rotation", 0, 180);
                    break;
            }

        } else if (state == CurrentState.BOUNCE) {
            switch (currentShape) {
                case Triangle:
                    rotationAnimator = ObjectAnimator.ofFloat(shapeView, "rotation", -360, 0);
                    break;
                case Rectangle:
                    rotationAnimator = ObjectAnimator.ofFloat(shapeView, "rotation", 180, 0);
                    break;
                case Circle:
                    rotationAnimator = ObjectAnimator.ofFloat(shapeView, "rotation", 180, 0);
                    break;
            }
        }

        return rotationAnimator;
    }

    private boolean isStopAnimator = false;

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE) {
            super.setVisibility(INVISIBLE);//不执行gone,因为gone会导致整个布局重新测量绘制
            //清理动画
            shapeView.clearAnimation();//此时动画扔残留在内存中继续执行
            loadShadow.clearAnimation();

            isStopAnimator=true;//用代码控制组织动画继续执行

            //把loadview从父布局移除
            ViewGroup parent = (ViewGroup) getParent();
            if (parent != null) {
                parent.removeView(this);//从父布局中移除自己
                removeAllViews();//移除自己所有的view
            }


        } else {
            super.setVisibility(visibility);
        }
    }
}
