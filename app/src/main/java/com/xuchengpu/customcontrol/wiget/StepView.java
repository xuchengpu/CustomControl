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
import android.view.View;

import com.xuchengpu.customcontrol.R;


/**
 * Created by 许成谱 on 2018/3/22 17:46.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class StepView extends View {
    private int mInnerColor = Color.BLUE;
    private int mOutColor = Color.RED;
    private int mStepColor = Color.GREEN;
    private int mBorderWidth = 8;
    private int mStepSize;

    private Paint mInnerPaint;
    private Paint mOutPaint;
    private Paint mStepPaint;

    private int maxStep;
    private int currentStep;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mInnerColor = array.getColor(R.styleable.StepView_innercolor, mInnerColor);
        mOutColor = array.getColor(R.styleable.StepView_outcolor, mOutColor);
        mStepColor = array.getColor(R.styleable.StepView_stepcolor, mStepColor);
        mBorderWidth = array.getDimensionPixelSize(R.styleable.StepView_borderwidth, mBorderWidth);
        mStepSize = array.getDimensionPixelSize(R.styleable.StepView_stepsize, mStepSize);
        array.recycle();

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOutColor);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setStyle(Paint.Style.STROKE);

        mStepPaint=new Paint();
        mStepPaint.setAntiAlias(true);
        mStepPaint.setColor(mStepColor);
        mStepPaint.setTextSize(mStepSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rect = new RectF(mBorderWidth / 2, mBorderWidth / 2, getWidth() - mBorderWidth / 2, getHeight() - mBorderWidth / 2);
        canvas.drawArc(rect, 135, 270, false, mInnerPaint);


        if(maxStep<=0) {
            return;
        }
        float angle = (float) currentStep / maxStep * 270;
        canvas.drawArc(rect, 135, angle, false, mOutPaint);

        String mCurrentStep = String.valueOf(currentStep);
        Rect bounds=new Rect();
        mStepPaint.getTextBounds(mCurrentStep,0,mCurrentStep.length(),bounds);

        int x=getWidth()/2-bounds.width()/2;

        Paint.FontMetricsInt fontMetricsInt = mStepPaint.getFontMetricsInt();
        int dy=(fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom;

        int y=getHeight()/2+dy;

        canvas.drawText(mCurrentStep,x,y,mStepPaint);



    }

    public synchronized void  setMaxStep(int maxStep){
        this.maxStep = maxStep;
    }

    public synchronized void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        invalidate();
    }
}
