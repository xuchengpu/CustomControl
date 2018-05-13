package com.xuchengpu.customcontrol.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.utils.BeizierTypeEvaluator;

import java.util.Random;

/**
 * Created by 许成谱 on 2018/5/13 19:47.
 * qq:1550540124
 * for:热爱生活每一天！
 * 自定义心形点赞效果view
 * 分析：此动画效果是一个三阶的贝塞尔曲线，有两个起始点，两个控制点。没有用函数曲线是因为函数曲线都太规则了，形成的效果不好看
 */

public class LovePraiseView extends RelativeLayout {
    private int viewWidth, viewHeight;//控件的宽高
    private Context mContext;
    private int[] images = new int[]{R.drawable.pl_blue, R.drawable.pl_red, R.drawable.pl_yellow};//点赞的几个图片

    private PointF point0;//起始点，底部终点
    private PointF point3;//终点，顶部随机点

    private Interpolator[] mInterpolator;


    public LovePraiseView(Context context) {
        this(context, null);
    }

    public LovePraiseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LovePraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        mInterpolator = new Interpolator[]{new AccelerateDecelerateInterpolator(), new AccelerateInterpolator(),
                new DecelerateInterpolator(), new LinearInterpolator()};
    }

    private void initPoint() {
        point0 = new PointF();
        point3 = new PointF();


        point0.x = viewWidth / 2;
        point0.y = viewHeight;

        point3.x = new Random().nextInt(viewWidth);
        point3.y = 0;
    }

    /**
     * 拿到控件宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    /**
     * 点赞，即给this添加子imageview 并设置动画效果
     */
    public void parise() {
        initPoint();
        ImageView targetView = getImageView();
        addView(targetView);//添加到此viewgroup中
        doAnimator(targetView);//开始执行动画
    }

    /**
     * 执行先放大、再向上曲线漂移、最后渐变半透明的动画
     *
     * @param targetView
     */
    private void doAnimator(final ImageView targetView) {
        //放大动画
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(targetView, "scaleX", 0, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(targetView, "scaleY", 0, 1f);
        //x、y方向缩放动画放一起
        AnimatorSet scaleSet = new AnimatorSet();
        scaleSet.setDuration(300);
        scaleSet.playTogether(animatorX, animatorY);

        //贝塞尔曲线动画  使用估值器:我们平时所用的addUpdateListener()所得到的数值就是从估值器中得来的 系统也默认给我们提供了几个估值器
        //重新设置控制点
        PointF point1 = new PointF();//控制点1
        PointF point2 = new PointF();//控制点2

        point1.x = new Random().nextInt(viewWidth);
        point1.y = new Random().nextInt(viewHeight / 2)+ viewHeight / 2;

        point2.x = new Random().nextInt(viewWidth);
        point2.y = new Random().nextInt(viewHeight / 2) ;//保证控制点3在控制点1的上方

        //获取图片的宽高
        //图片的宽高
        Bitmap bitmap = convertViewToBitmap(targetView);
        final int ivHeight = bitmap.getHeight();
        final int ivWidth = bitmap.getWidth();

        TypeEvaluator typeEvaluator = new BeizierTypeEvaluator(point1, point2);
        //通过这个公式传入的p0、p3能传入typeEvaluator实例中，与p1、p2并肩作战。估值器能根据几个值算出目标值传给UpdateListener()
        ValueAnimator valueAnimator = ValueAnimator.ofObject(typeEvaluator, point0, point3);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF value = (PointF) animation.getAnimatedValue();
                //设置目标view沿着贝塞尔曲线移动
//                Log.e("TAG", "point3x=="+point3.x);
//                Log.e("TAG", "point3y=="+point3.y);
                targetView.setX(value.x - ivWidth / 2);
                targetView.setY(value.y - ivHeight / 2);
                //设置透明度
                float t = animation.getAnimatedFraction();
                targetView.setAlpha(1 - t + 0.2f);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画执行完毕 移除imageview
                removeView(targetView);
            }
        });
        //一起执行
        AnimatorSet allSet = new AnimatorSet();
        //设置执行先后顺序
        allSet.playSequentially(scaleSet, valueAnimator);
        allSet.setInterpolator(mInterpolator[new Random().nextInt(mInterpolator.length)]);
        allSet.start();

    }

    private ImageView getImageView() {
        ImageView imageView = new ImageView(mContext);
        //设置居中等属性
        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);//底部对齐
        params.addRule(CENTER_HORIZONTAL);//水平居中
        imageView.setLayoutParams(params);
        //设置图片背景
        Drawable drawable = ContextCompat.getDrawable(mContext, images[new Random().nextInt(images.length)]);
        imageView.setBackground(drawable);
        return imageView;
    }

    /**
     * 获取任何一个view的bitmap图片
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);//清除画图缓冲区,不然下一次获取的图片还是上一次的
        return bitmap;
    }
}
