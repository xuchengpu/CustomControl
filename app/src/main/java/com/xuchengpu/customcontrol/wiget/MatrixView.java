package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xuchengpu.customcontrol.R;

/**
 * Created by 许成谱 on 2018/10/31 17:06.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class MatrixView extends View {
    private final Context mContext;
    private Bitmap mBitmap;
    private Paint paint;
    private Matrix mPolyMatrix;

    public MatrixView(Context context) {
        this(context,null);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.meinv);
        mBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);
        paint = new Paint();
        initBitmapAndMatrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float[] mImageMatrix2=new float[]{1,0,0,0,1,0,0,0,1};
        Matrix matrix2=new Matrix();
        matrix2.setValues(mImageMatrix2);
        canvas.drawBitmap(mBitmap,matrix2,paint);

        float[] mImageMatrix=new float[]{1,0,400,0,1,400,0,0,1};
        Matrix matrix=new Matrix();
        matrix.setValues(mImageMatrix);
        canvas.drawBitmap(mBitmap,matrix,paint);

        // 根据Matrix绘制一个变换后的图片
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);
    }
    private void initBitmapAndMatrix() {

        mPolyMatrix = new Matrix();


        float[] src = {0, 0,                                    // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()};                        // 左下

        float[] dst = {0, 0,                                    // 左上
                mBitmap.getWidth(), 400,                        // 右上
                mBitmap.getWidth(), mBitmap.getHeight() - 200,  // 右下
                0, mBitmap.getHeight()};                        // 左下

        // 核心要点
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1); // src.length >> 1 为位移运算 相当于处以2

        // 此处为了更好的显示对图片进行了等比缩放和平移(图片本身有点大)
//        mPolyMatrix.postScale(0.26f, 0.26f);
        mPolyMatrix.postTranslate(300,800);
    }
}
