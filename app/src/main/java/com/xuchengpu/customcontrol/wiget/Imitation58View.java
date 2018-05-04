package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static com.xuchengpu.customcontrol.wiget.Imitation58View.Shape.Circle;
import static com.xuchengpu.customcontrol.wiget.Imitation58View.Shape.Rectangle;
import static com.xuchengpu.customcontrol.wiget.Imitation58View.Shape.Triangle;

/**
 * Created by 许成谱 on 2018/3/26 18:44.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class Imitation58View extends View {
    private Shape shape = Circle;
    private float size;

    private Paint mPaint;
    private Path path;
    private int borderWidth=30;

    public Imitation58View(Context context) {
        this(context, null);
    }

    public Imitation58View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Imitation58View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();

        mPaint.setStrokeWidth(borderWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float width = MeasureSpec.getSize(widthMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);
        size = width > height ? height : width;
        setMeasuredDimension((int)size, (int)size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (shape) {
            case Circle:
                //画圆
                mPaint.setColor(Color.RED);
                canvas.drawCircle(size / 2, size / 2, size/2-borderWidth/2, mPaint);
                break;
            case Triangle:
                //画三角形
                mPaint.setColor(Color.GREEN);
                if (path == null) {
                    path = new Path();
                    path.moveTo(size / 2, borderWidth/2);
                    path.lineTo(borderWidth/2, (float) Math.sqrt(3) * size/2);
                    path.lineTo(size-borderWidth/2, (float) Math.sqrt(3) * size/2);
                    path.close();
                }
                canvas.drawPath(path, mPaint);
                break;
            case Rectangle:
                //画正方形
                mPaint.setColor(Color.BLUE);
                canvas.drawRect(borderWidth/2, borderWidth/2, size-borderWidth/2, size-borderWidth/2, mPaint);
                break;

        }
    }

    public enum Shape {
        Triangle, Rectangle, Circle;
    }

    public void change() {
        switch (shape) {
            case Triangle:
                shape=Rectangle;
                break;
            case Rectangle:
                shape=Circle;
                break;
            case Circle:
                shape=Triangle;
                break;
        }
        invalidate();
    }
    public Shape getCurrentShape(){
        return shape;
    }

}
