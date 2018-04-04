package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/3/20 9:21.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class TextView extends View {
    private String mText;
    private int mTextSize = 15;
    private int mTextColor = Color.BLACK;
    private Paint mPaint;

    public TextView(Context context) {
        this(context, null);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextView);
        mText = array.getString(R.styleable.TextView_mtext);
        mTextColor = array.getColor(R.styleable.TextView_mtextcolor, mTextColor);

        mTextSize = array.getDimensionPixelSize(R.styleable.TextView_mtextsize, dp2px(mTextSize));
        //回收
        array.recycle();
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

    }

    private int dp2px(int mTextSize) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics());
    }

    //自定义view的测量方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //布局的宽高都是由这个方法决定
        //指定控件的宽高，需要测量
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        //1、确定的值，这个时候不需要计算，给的多少就是多少
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //2、给的是wrap_content需要计算
        if (widthMode == MeasureSpec.AT_MOST) {
            //计算的宽度 与字体的长度、大小有关 用画笔来测量
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            withSize = bounds.width();
        }
        if (heightMode ==MeasureSpec.AT_MOST||heightMode==MeasureSpec.UNSPECIFIED) {
            //计算的宽度 与字体的长度、大小有关 用画笔来测量
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            heightSize = bounds.height();
        }
        setMeasuredDimension(withSize + getPaddingLeft() + getPaddingRight(), heightSize + getPaddingBottom() + getPaddingTop());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
        float baseline = getHeight() / 2 + dy;
        //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
        canvas.drawText(mText, getPaddingLeft(), baseline, mPaint);//绘制文字
//        canvas.drawArc();//绘制圆弧
//        canvas.drawCircle();//绘制圆
//        canvas.drawBitmap();//绘制图片
//        canvas.drawLine();//绘制直线
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
