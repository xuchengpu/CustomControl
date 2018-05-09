package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 许成谱 on 2018/5/9 22:40.
 * qq:1550540124
 * for:热爱生活每一天！
 */

public class BeiSaiErView extends View {
    private Paint mPaint;
    private Point fixationPoint, dragPoint;//固定点，移动点
    private int mDistance;//固定点与移动点的实时距离
    private int maxRadius, minRadius;//固定点的最大、最小圆半径
    private int fixationRadius;//固定点圆的半径

    public BeiSaiErView(Context context) {
        this(context, null);
    }

    public BeiSaiErView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeiSaiErView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        if (fixationPoint == null || dragPoint == null) {
            return;
        }
        //两个点之间的距离
        mDistance = (int) Math.sqrt((dragPoint.x - fixationPoint.x) * (dragPoint.x - fixationPoint.x) + (dragPoint.y - fixationPoint.y) * (dragPoint.y - fixationPoint.y));
        //画移动圆
        canvas.drawCircle(dragPoint.x, dragPoint.y, maxRadius, mPaint);
        //画固定圆
        //固定圆半径
        fixationRadius = maxRadius - mDistance / 14;
        if (fixationRadius > minRadius) {
            canvas.drawCircle(fixationPoint.x, fixationPoint.y, fixationRadius, mPaint);
            //画贝瑟尔曲线
             Path path = getBeiSaiErPath();
            if(path!=null) {
                canvas.drawPath(path, mPaint);
            }

        }

    }

    private Path getBeiSaiErPath() {
        Path path = new Path();
        //tana=dy/dx
        int dx = dragPoint.x - fixationPoint.x;
        if(dx<=0) {
            return null;
        }
        double a = Math.atan((dragPoint.y - fixationPoint.y) / (dx));
        int x;
        int y;
        //p0坐标
        x = (int) (fixationPoint.x + fixationRadius * Math.sin(a));
        y = (int) (fixationPoint.y - fixationRadius * Math.cos(a));
        Point p0 = new Point(x,y);

        //p1坐标
        x = (int) (dragPoint.x + maxRadius * Math.sin(a));
        y = (int) (dragPoint.y - maxRadius * Math.cos(a));
        Point p1 = new Point(x,y);

        //p2坐标
        x = (int) (dragPoint.x - maxRadius * Math.sin(a));
        y = (int) (dragPoint.y +maxRadius * Math.cos(a));
        Point p2 = new Point(x,y);

        //p3坐标
        x = (int) (fixationPoint.x - fixationRadius * Math.sin(a));
        y = (int) (fixationPoint.y + fixationRadius * Math.cos(a));
        Point p3 = new Point(x,y);

        path.moveTo(p0.x,p0.y);
        //控制点+终点
        Point controlPoint = getControlPoint();
        path.quadTo(controlPoint.x,controlPoint.y,p1.x,p1.y);
        path.lineTo(p2.x,p2.y);
        path.quadTo(controlPoint.x,controlPoint.y,p3.x,p3.y);
        path.close();
        return path;
    }

    private Point getControlPoint() {
        return new Point((dragPoint.x+fixationPoint.x)/2,(dragPoint.y+fixationPoint.y)/2);
    }
    float startX;
    float startY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX=event.getX();
                startY=event.getY();
                initFixationPoint(startX, startY);
                break;
            case MotionEvent.ACTION_MOVE:
                initDragPoint(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if(fixationRadius > minRadius) {
                    initDragPoint(startX, startY);
                }

                break;
        }
        invalidate();
        return true;//表示消费该事件，否则down事件后 move与up事件不会从viewgroup再向下传递过来

    }

    private void initDragPoint(float x, float y) {
        dragPoint = new Point((int) x, (int) y);
    }

    private void initFixationPoint(float x, float y) {
        fixationPoint = new Point((int) x, (int) y);
    }
}
