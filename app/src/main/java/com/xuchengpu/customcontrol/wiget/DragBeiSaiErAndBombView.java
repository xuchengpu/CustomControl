package com.xuchengpu.customcontrol.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.xuchengpu.customcontrol.activitys.DragViewBombActivity;
import com.xuchengpu.customcontrol.utils.MyViewOnTouchListener;

/**
 * Created by 许成谱 on 2018/5/9 22:40.
 * qq:1550540124
 * for:热爱生活每一天！
 * 给任何一个view拖拽添加贝塞尔曲线效果
 * 使用方式简单明了：DragBeiSaiErAndBombView.attach(View targetview,Context context,BombListener listener);一行代码搞定
 */

public class DragBeiSaiErAndBombView extends View {
    private Paint mPaint;
    private Point fixationPoint, dragPoint;//固定点，移动点
    private int mDistance;//固定点与移动点的实时距离
    private int maxRadius, minRadius;//固定点的最大、最小圆半径
    private int fixationRadius;//固定点圆的半径
    private Bitmap bitmap;//移动的view影像

    public DragBeiSaiErAndBombView(Context context) {
        this(context, null);
    }

    public DragBeiSaiErAndBombView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBeiSaiErAndBombView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initData();

    }

    private void initData() {
        maxRadius = dp2px(8);
        minRadius = dp2px(3);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //手指还没有触碰的时候不执行下面代码
        if (fixationPoint == null || dragPoint == null) {
            return;
        }
        //两个点之间的距离
        mDistance = (int) Math.sqrt((dragPoint.x - fixationPoint.x) * (dragPoint.x - fixationPoint.x) + (dragPoint.y - fixationPoint.y) * (dragPoint.y - fixationPoint.y));

        //画固定圆
        //固定圆半径
        fixationRadius = maxRadius - mDistance / 30;
        if (fixationRadius > minRadius) {
            canvas.drawCircle(fixationPoint.x, fixationPoint.y, fixationRadius, mPaint);
            //画贝瑟尔曲线
            Path path = getBeiSaiErPath();
            if (path != null) {
                canvas.drawPath(path, mPaint);
            }
        }
        //画图片 位置也是手指移动的位置 , 中心位置才是手指拖动的位置
        if (bitmap != null) {
            // 搞一个渐变动画
            canvas.drawBitmap(bitmap, dragPoint.x - bitmap.getWidth() / 2, dragPoint.y - bitmap.getHeight() / 2, new Paint());
        }

    }

    private Path getBeiSaiErPath() {
        Path path = new Path();
        //tana=dy/dx
        int dx = dragPoint.x - fixationPoint.x;
        if (dx == 0) {//防止dy/dx 产生除0的情况崩溃
            return null;
        }
        double a = Math.atan((dragPoint.y - fixationPoint.y) / (dx));
        int x;
        int y;
        //p0坐标
        x = (int) (fixationPoint.x + fixationRadius * Math.sin(a));
        y = (int) (fixationPoint.y - fixationRadius * Math.cos(a));
        Point p0 = new Point(x, y);

        //p1坐标
        x = (int) (dragPoint.x + maxRadius * Math.sin(a));
        y = (int) (dragPoint.y - maxRadius * Math.cos(a));
        Point p1 = new Point(x, y);

        //p2坐标
        x = (int) (dragPoint.x - maxRadius * Math.sin(a));
        y = (int) (dragPoint.y + maxRadius * Math.cos(a));
        Point p2 = new Point(x, y);

        //p3坐标
        x = (int) (fixationPoint.x - fixationRadius * Math.sin(a));
        y = (int) (fixationPoint.y + fixationRadius * Math.cos(a));
        Point p3 = new Point(x, y);

        path.moveTo(p0.x, p0.y);
        //控制点+终点
        Point controlPoint = getControlPoint();
        path.quadTo(controlPoint.x, controlPoint.y, p1.x, p1.y);//获得贝塞尔曲线的path
        path.lineTo(p2.x, p2.y);//连成直线
        path.quadTo(controlPoint.x, controlPoint.y, p3.x, p3.y);
        path.close();//path闭合
        return path;
    }

    private Point getControlPoint() {
        return new Point((dragPoint.x + fixationPoint.x) / 2, (dragPoint.y + fixationPoint.y) / 2);
    }


    public void initDragPoint(float x, float y) {
        dragPoint = new Point((int) x, (int) y);
    }

    public void initFixationPoint(float x, float y) {
        fixationPoint = new Point((int) x, (int) y);
    }

    /**
     * 关联view,使得view可全屏拖动并实现贝塞尔曲线粘性效果、放开后爆炸效果。
     *
     * @param view
     */
    public static void attach(View view, Context context,DragViewBombActivity.BombListener listener) {
        DragBeiSaiErAndBombView dragBeiSaiErAndBombView = new DragBeiSaiErAndBombView(context);
        view.setOnTouchListener(new MyViewOnTouchListener(view, dragBeiSaiErAndBombView, context,listener));
    }


    /**
     * 设置移动的图片
     *
     * @param bitMapView
     */
    public void setBitmap(Bitmap bitMapView) {
        this.bitmap = bitMapView;
    }

    /**
     * 处理手指抬起时间
     */
    public void handleActionUp() {
        if (fixationRadius < minRadius) {//超出距离,爆炸效果
            if (listener != null) {
                listener.dismiss(dragPoint);
            }
        } else {//未超出距离，回归原位
            backToOriginPositon();
        }

    }

    /**
     * 回到原位
     */
    private void backToOriginPositon() {
        final Point tempPoint=new Point(dragPoint);
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(200);
        animator.setInterpolator(new OvershootInterpolator(3f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                updateDragPoint(percent,tempPoint);
            }
        });
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    //通知动画执行完毕
                    listener.restore();

                }
            }
        });

    }

    private void updateDragPoint(float percent, Point tempPoint) {
        Log.e("TAG", "percent:" + percent);
        float x = fixationPoint.x + (tempPoint.x - fixationPoint.x) * percent;
        float y = fixationPoint.y + (tempPoint.y - fixationPoint.y) * percent;
        initDragPoint(x, y);
        Log.e("TAG", "x=="+x+"-->y=="+y);
        invalidate();
    }


    /**
     * 监听this
     */
    public interface ViewListener {
        /**
         * 复位
         */
        void restore();

        /**
         * 消失、爆炸
         */
        void dismiss(Point point);
    }

    private ViewListener listener;

    public void setOnViewListener(ViewListener listener) {
        this.listener = listener;
    }

}
