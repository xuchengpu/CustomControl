package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/3/27 15:58.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class RatingView extends View {
    private Bitmap bgBitmap;
    private Bitmap forBitmap;
    private int count;
    private Paint mPaint;
    private int currentIndex = 0;


    public RatingView(Context context) {
        this(context, null);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingView);
        count = array.getInt(R.styleable.RatingView_count, 0);
        int bgDrawable = array.getResourceId(R.styleable.RatingView_backgroundbitmap, 0);
        if (bgDrawable == 0) {
            throw new RuntimeException("背景图片不能为空");
        }
        bgBitmap = BitmapFactory.decodeResource(getResources(), bgDrawable);


        int forDrawable = array.getResourceId(R.styleable.RatingView_foregroundbitmap, 0);
        if (forDrawable == 0) {
            throw new RuntimeException("选中展示的图片不能为空");
        }
        forBitmap = BitmapFactory.decodeResource(getResources(), forDrawable);
        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();
        setMeasuredDimension(width * count + getPaddingRight() + getPaddingLeft() * count, height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < count; i++) {
            if (currentIndex > i) {
                canvas.drawBitmap(forBitmap, getPaddingLeft()+(getPaddingLeft() + bgBitmap.getWidth()) * i, getPaddingTop(), mPaint);
            } else {
                canvas.drawBitmap(bgBitmap, getPaddingLeft()+(getPaddingLeft() + bgBitmap.getWidth()) * i, getPaddingTop(), mPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                int index = (int) x / (bgBitmap.getWidth() + getPaddingRight() / count + getPaddingLeft())+1;

                //屏蔽非法值
                if(index<0) {
                    index=0;
                }else if(index>6) {
                    index=6;
                }
                //优化内存 减少绘制次数
                if (currentIndex == index) {
                    break;
                }
                currentIndex = index;
                invalidate();
                break;
        }
        return true;//只有为TRUE时后续的move的时间才会继续传进来，否则系统判断当down时间返回的是super或者false时，后续的move事件就不会再传进来了
    }
}
