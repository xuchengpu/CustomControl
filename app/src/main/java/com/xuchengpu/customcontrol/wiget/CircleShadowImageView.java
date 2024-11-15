package com.xuchengpu.customcontrol.wiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2019/12/11 17:21.
 * qq:1550540124
 * 热爱生活每一天！
 */
@SuppressLint("AppCompatCustomView")
public class CircleShadowImageView extends View {
    private Bitmap mBitmap;
    private int mLineColor;//圆环颜色
    private int mLineWidth;//圆环宽度
    private Paint mPaint = new Paint();
    private Context mContext;
    private int shadowWidth = 0;//阴影宽度

    public CircleShadowImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleShadowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleShadowImageView);
        BitmapDrawable drawable = (BitmapDrawable) ta.getDrawable(R.styleable.CircleShadowImageView_src);
        if (drawable != null)
            mBitmap = drawable.getBitmap();
        mLineColor = ta.getColor(R.styleable.CircleShadowImageView_line_color, Color.WHITE);
        mLineWidth = (int) ta.getDimension(R.styleable.CircleShadowImageView_line_width, 0);
        shadowWidth = (int) ta.getDimension(R.styleable.CircleShadowImageView_shadow_width, dip2px(15));
        squareBitmap();
        ta.recycle();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);
    }

    /**
     * 将图片处理为正方形图片
     */
    private void squareBitmap() {
        if (mBitmap != null && mBitmap.getWidth() != mBitmap.getHeight()) {
            int wh = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
            int num = Math.abs((mBitmap.getWidth() - mBitmap.getHeight()) / 2);
            if (mBitmap.getHeight() >= mBitmap.getWidth()) {
                mBitmap = Bitmap.createBitmap(mBitmap, 0, num, wh, wh);
            } else {
                mBitmap = Bitmap.createBitmap(mBitmap, num, 0, wh, wh);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            int w = getWidth() - getPaddingLeft() - getPaddingRight() - shadowWidth * 2;
            int h = getHeight() - getPaddingTop() - getPaddingBottom() - shadowWidth * 2;
            int bw = mBitmap.getWidth();
            /**
             * 根据画布大小缩放图片
             */
            Matrix matrix = new Matrix();
            if (w >= h) {
                matrix.setScale(h / (float) bw, h / (float) bw);
            } else {
                matrix.setScale(w / (float) bw, w / (float) bw);
            }
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, bw, bw, matrix, true);
            int width = 0;
            int left;
            int top;
            /**
             * 确定图片的位置，这里考虑了设置的padding，如果不处理padding的值，那么设置的padding将失效
             */
            if (mBitmap != null)
                width = mBitmap.getWidth();
            if (w >= h) {
                left = getPaddingLeft() + Math.abs((w - h) / 2);
                top = getPaddingTop();
            } else {
                left = getPaddingLeft();
                top = getPaddingTop() + Math.abs((w - h) / 2);
            }
            //画阴影
            mPaint.setShadowLayer(shadowWidth * 2 / 3, 0, 0, Color.BLACK);
            canvas.drawCircle((width / 2) + left + shadowWidth, width / 2 + top + shadowWidth, width / 2 - mLineWidth, mPaint);
            //画图像
            mPaint.clearShadowLayer();
            canvas.drawBitmap(createCircleImage(mBitmap, width), left + shadowWidth, top + shadowWidth, mPaint);
            //画环
            if (mLineWidth != 0) {
                mPaint.setColor(mLineColor);
                mPaint.setStrokeWidth(mLineWidth);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setAntiAlias(true);
                canvas.drawCircle((width / 2) + left + shadowWidth, width / 2 + top + shadowWidth, width / 2 - mLineWidth, mPaint);
            }
        }
    }

    /**
     * 绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2 - mLineWidth, p);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, p);
        return target;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        int pd = getPaddingBottom();
        int pt = getPaddingTop();
        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int width = 0;
        int height = 0;
        /**
         * 获取设置的图片的宽高
         */
        if (mBitmap != null) {
            width = mBitmap.getWidth();
            height = mBitmap.getHeight();
        }
        /**
         *当宽、高设置为wrap_content时，Mode为MeasureSpec.AT_MOST最大模式；
         *当宽、高设置为match_parent或者具体的值时，Mode为MeasureSpec.EXACTLY精确模式；
         *在测量View的宽、高时，需要对padding做处理，不然设置的padding将会失效
         */
        if (widthMeasureMode == MeasureSpec.AT_MOST && heightMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width + pl + pr, height + pd + pt);
        } else if (widthMeasureMode == MeasureSpec.AT_MOST && heightMeasureMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(width + pl + pr, heightMeasureSize);
        } else if (widthMeasureMode == MeasureSpec.EXACTLY && heightMeasureMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthMeasureSize, heightMeasureSize);
        } else if (widthMeasureMode == MeasureSpec.EXACTLY && heightMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSize, height + pd + pt);
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        recycleBitmap();
        this.mBitmap = bitmap;
        squareBitmap();
        invalidate();
    }

    public void setImageDrawable(Drawable drawable) {
        setImageBitmap(drawable == null ? null : drawableToBitmap(drawable));
    }

    public void setImageResource(int resId) {
        setImageDrawable(ContextCompat.getDrawable(mContext, resId));
    }

    public void setLineColorResource(int resId) {
        setLineColor(ContextCompat.getColor(mContext, resId));
    }

    public void setLineColor(int color) {
        mLineColor = color;
        invalidate();
    }

    /**
     * 设置线的宽度
     *
     * @param width 这个宽度的单位是px
     */
    public void setLineWidth(int width) {
        mLineWidth = width;
        invalidate();
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();

            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);

            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);

            return bitmap;
        } else {
            return null;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleBitmap();
    }

    /**
     * 释放图片资源
     */
    private void recycleBitmap() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
    private int dip2px(float dpValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (int) (dpValue * scale + 0.5F);
    }
}
