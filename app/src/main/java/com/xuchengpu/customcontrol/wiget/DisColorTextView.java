package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/3/23 17:01.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class DisColorTextView extends android.support.v7.widget.AppCompatTextView {
    private int originColor;
    private int changeColor=Color.RED;

    private Paint originPaint;
    private Paint changePaint;
    private float progress=1;
    private Oritation oritation=Oritation.LEFT_TO_RIGHT;
    private float textsize=getTextSize();

    public enum Oritation{
        LEFT_TO_RIGHT,RIGHT_TO_LEFT
    }

    public DisColorTextView(Context context) {
        this(context,null);
    }

    public DisColorTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DisColorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.DisColorTextView);
        originColor=array.getColor(R.styleable.DisColorTextView_origincolor,getTextColors().getDefaultColor());
        changeColor=array.getColor(R.styleable.DisColorTextView_changecolor,changeColor);
        array.recycle();

        originPaint=getPaintByColor(originColor);
        changePaint=getPaintByColor(changeColor);
    }

    private Paint getPaintByColor(int color) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);//防抖动
        paint.setTextSize(textsize);
        paint.setColor(color);
        return paint;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        if(TextUtils.isEmpty(text)) {
            return;
        }
        if(oritation==Oritation.LEFT_TO_RIGHT) {
            drawText(canvas,text,originPaint,0,progress*getWidth());
            drawText(canvas,text,changePaint,progress*getWidth(),getWidth());
        }else{
            drawText(canvas,text,changePaint,0,progress*getWidth());
            drawText(canvas,text,originPaint,progress*getWidth(),getWidth());
        }

    }


    private void drawText(Canvas canvas,String text,Paint paint,float start,float end) {
        canvas.save();
        canvas.clipRect(start,0,end,getHeight());
        Rect boounds=new Rect();
        paint.getTextBounds(text,0,text.length(),boounds);
        int width = boounds.width();
        float x=getWidth()/2-width/2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseLine=getHeight()/2+dy;
        canvas.drawText(text,x,baseLine,paint);
        canvas.restore();
    }
    public void setProgress(float progress){
        this.progress=progress;
        invalidate();
    }
    public void setOritation(Oritation oritation){
        this.oritation=oritation;
    }
//    public void setChangeColor (int changeColor){
//        this.changeColor=changeColor;
//    }

}
