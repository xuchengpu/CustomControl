package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许成谱 on 2018/3/28 11:09.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class IndexView extends View {
    private List<String> letters;
    private Paint primaryPaint;
    private Paint selectPaint;
    private int selectPosition = -1;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化字符
        letters = new ArrayList();
        char l = 'A';
        for (int i = 0; i < 26; i++) {
            letters.add(String.valueOf(l));
            l += 1;
        }
        letters.add("#");

        primaryPaint = new Paint();
        primaryPaint.setAntiAlias(true);
        primaryPaint.setTextSize(sp2px(12));
        primaryPaint.setColor(Color.BLACK);

        selectPaint = new Paint();
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(sp2px(16));
        selectPaint.setColor(Color.BLUE);
    }

    private float sp2px(int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, i, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        String a = "A";
        Rect bounds = new Rect();
        primaryPaint.getTextBounds(a, 0, a.length(), bounds);

        int width = getPaddingRight() + getPaddingLeft() + bounds.width();

        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect bounds = new Rect();
        for (int i = 0; i < letters.size(); i++) {

            primaryPaint.getTextBounds(letters.get(i), 0, letters.get(i).length(), bounds);
            int x = getWidth() / 2 - bounds.width() / 2;
            float itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.size();
            Paint.FontMetrics metrics = primaryPaint.getFontMetrics();
            float dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom;
            int y = (int) (itemHeight * i + getPaddingTop()+dy);
            if (selectPosition == i) {
                canvas.drawText(letters.get(i), x, y, selectPaint);
            } else {
                canvas.drawText(letters.get(i), x, y, primaryPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.size();
                int position = (int) ((y - getPaddingTop()) / itemHeight);
                if (position < 0) {
                    position = 0;
                }
                if (position > letters.size() - 1) {
                    position = letters.size() - 1;
                }
                if(position==selectPosition) {
                    break;
                }
                selectPosition = position;

                listener.Touch(letters.get(selectPosition));
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                selectPosition = -1;
                listener.up();
                invalidate();
                break;
        }
        return true;
    }
    private OnPositonListener listener;
    public void setOnPositonListener(OnPositonListener listener){
        this.listener=listener;
    }
    public interface OnPositonListener{
        void Touch(String letter);
        void up();
    }
}
