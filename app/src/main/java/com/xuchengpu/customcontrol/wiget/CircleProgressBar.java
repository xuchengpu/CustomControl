package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/3/26 17:13.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class CircleProgressBar extends View {
    private int innerColor = Color.GRAY;
    private int outColor = Color.RED;
    private int textSize = 10;
    private int textColor = Color.RED;
    private int borderWidth = 4;

    private Paint innerPaint;
    private Paint outPaint;
    private Paint textPaint;

    private float progress = 0;
    private float maxValue;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        innerColor = array.getColor(R.styleable.CircleProgressBar_minnercolor, innerColor);
        outColor = array.getColor(R.styleable.CircleProgressBar_moutcolor, outColor);
        textColor = array.getColor(R.styleable.CircleProgressBar_textcolor, textColor);
        borderWidth = array.getDimensionPixelSize(R.styleable.CircleProgressBar_mborderwith, dp2px(borderWidth));
        textSize = array.getDimensionPixelSize(R.styleable.CircleProgressBar_textsize, sp2px(textSize));
        array.recycle();

        innerPaint = new Paint();
        innerPaint.setColor(innerColor);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);
        innerPaint.setAntiAlias(true);
        innerPaint.setStrokeWidth(borderWidth);
        innerPaint.setStyle(Paint.Style.STROKE);

        outPaint = new Paint();
        outPaint.setColor(outColor);
        outPaint.setAntiAlias(true);
        outPaint.setStrokeCap(Paint.Cap.ROUND);
        outPaint.setStrokeWidth(borderWidth);
        outPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.STROKE);
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(size, size);//保证是正方形
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画内圆
        int center = getWidth() / 2;
        canvas.drawCircle(center, center, center - borderWidth / 2, innerPaint);

        //画外圆
        float precent;
        if (maxValue <= 0) {
            precent = 0;
        } else {
            precent =  (progress / maxValue * 360);
        }
        RectF rect = new RectF(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2, getWidth() - borderWidth / 2);
        canvas.drawArc(rect, 0, precent, false, outPaint);

        //画文字
        String text = String.valueOf((int)(precent/3.6))+"%";
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int textWidth = bounds.width();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, baseLine, textPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
