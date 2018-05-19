package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 许成谱 on 2018/5/19 15:52.
 * qq:1550540124
 * for:热爱生活每一天！
 * 圆形扩散动画
 */

public class CircleDiffusionView extends View {
    private Paint mPaint;
    private float radius = 0;

    public CircleDiffusionView(Context context) {
        this(context, null);
    }

    public CircleDiffusionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleDiffusionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));//注释的几个部分整体组成第二种实现方式

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);//反奇偶规则,实现圆外为白色填充，圆内透明  实现这个效果需关闭硬件加速
        path.addCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, Path.Direction.CW);
        canvas.drawPath(path, mPaint);
//        canvas.drawARGB(255,255,255,255);
//        canvas.translate(getMeasuredWidth()/2,getMeasuredHeight()/2);
//        canvas.drawCircle(0,0,radius,mPaint);

    }

    /**
     * 对外提供接口设置圆的半径，实现扩散效果
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }
}
