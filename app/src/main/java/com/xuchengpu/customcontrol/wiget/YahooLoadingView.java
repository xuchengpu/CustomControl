package com.xuchengpu.customcontrol.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.xuchengpu.customcontrol.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 许成谱 on 2018/5/19 8:23.
 * qq:1550540124
 * for:热爱生活每一天！
 * 先在xml最上层布置此view
 * 实例化后调用方法：开始加载时调用startLoading
 * 加载完成时调用stopLoading
 */

public class YahooLoadingView extends RelativeLayout {
    private final Context mContext;
    private int circleRadius;//小圆的半径 dp
    private int radius;//旋转的大圆半径  dp
    private int[] colors = new int[]{R.color.VCheck_blue, R.color.VCheck_green, R.color.VCheck_yellow, R.color.red, R.color.colorAccent, R.color.colorPrimary};
    private int mWidth;
    private int mHeight;
    private int circleNum = 9;//圆的个数
    private int rotateDuration = 3000;//旋转时间 ms
    private int aggregationDuration = 2000;//聚合时间
    private int diffusionDuration = 500;//扩散动画时间
    private List<CircleView> circleViews;//用来装留个圆 方便进行移动或者移除
    private CircleDiffusionView circleDiffusionView;//扩散的view
    private ValueAnimator rotateAnimator;

    public YahooLoadingView(Context context) {
        this(context, null);
    }

    public YahooLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YahooLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        circleRadius = dp2px(8);
        radius = dp2px(80);
        circleViews = new ArrayList<>();
        addBackgroundView();//一进来就添加扩散view  该view默认显示白色
    }

    private void addBackgroundView() {
        circleDiffusionView = new CircleDiffusionView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        circleDiffusionView.setLayoutParams(params);
        addView(circleDiffusionView);
    }

    /**
     * 当视图大小发生变化时调用
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * 1、开始加载，外界开始调用的地方
     */
    public void startLoading() {
        //一进来先设置为可见
        setVisibility(VISIBLE);
        //添加圆
        addCircleView();
        //开始旋转动画
        startRotate();
    }

    /**
     * 2、加载完成，由外界调用
     */
    public void stopLoading() {
        //先取消旋转动画
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
        }
        //开始聚合动画
        startAggregation();
    }


    /**
     * 开始旋转动画
     */
    private void startRotate() {
        rotateAnimator = ValueAnimator.ofFloat(0, (float) Math.PI * 4);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float) animation.getAnimatedValue();
                for (int i = 0; i < circleViews.size(); i++) {
                    setLocation(i, radius, angle);
                }
            }
        });
        rotateAnimator.setRepeatCount(10000);//重复执行
        rotateAnimator.setDuration(rotateDuration);
        rotateAnimator.start();
    }

    /**
     * 聚合收拢动画
     */
    private void startAggregation() {

        ValueAnimator animator = ValueAnimator.ofFloat(radius, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeCircleView();//动画执行完毕先移除掉circleview
                startDiffusion();//开始扩散动画
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float radius = (float) animation.getAnimatedValue();
                for (int i = 0; i < circleViews.size(); i++) {
                    setLocation(i, (int) radius, 0);
                }
            }
        });
        animator.setDuration(aggregationDuration);
        animator.setInterpolator(new AnticipateInterpolator());
        animator.start();
    }

    /**
     * 开始扩散动画
     */
    private void startDiffusion() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, (float) Math.sqrt(getMeasuredHeight() * getMeasuredHeight() / 4 + getMeasuredWidth() * getMeasuredWidth() / 4));
        animator.setDuration(diffusionDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float radius = (float) animation.getAnimatedValue();
                circleDiffusionView.setRadius(radius);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(circleDiffusionView);
                setVisibility(GONE);

            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    /**
     * 设置位置 注意mWidth、mHeight只有在布局加载完毕之后才有效，否则为0
     * @param index
     * @param r
     * @param startAngle
     */
    private void setLocation(int index, int r, float startAngle) {
        double a = Math.PI * 2 / circleNum * index;//注意这里的单位是π
        circleViews.get(index).setX((float) (mWidth / 2 - circleRadius + r * Math.cos(-startAngle + a)));
        circleViews.get(index).setY((float) (mHeight / 2 - circleRadius - r * Math.sin(-startAngle + a)));
    }

    private void addCircleView() {
        //添加之前先清除原有的
        removeCircleView();


        for (int i = 0; i < circleNum; i++) {
            CircleView circleView = new CircleView(mContext);
            //设置宽高
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = circleRadius * 2;
            params.height = params.width;
            circleView.setLayoutParams(params);
            //设置随机颜色
            int index = new Random().nextInt(colors.length);
            Log.e("TAG", "index==" + index);
            circleView.changeColor(ContextCompat.getColor(mContext, colors[index]));
            //添加到布局中
            addView(circleView);
            circleViews.add(circleView);
            //设置位置
            setLocation(i, radius, 0);
        }
    }

    private void removeCircleView() {
        for (int i = 0; i < circleViews.size(); i++) {
            removeView(circleViews.get(i));
        }
        circleViews.clear();
    }
}
