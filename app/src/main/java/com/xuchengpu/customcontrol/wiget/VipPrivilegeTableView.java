package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by 许成谱 on 2018/5/7 21:28.
 * qq:1550540124
 * for:热爱生活每一天！
 * 自定义会员特权列表view
 */

public class VipPrivilegeTableView extends View {
    private int widgetLength;//控件边长
    private int tableLength;//表格边长
    private Paint mTablePaint;//表格边框画笔
    private Paint mTitilePaint;//小标题画笔
    private Paint mSmallTitilePaint;//标题画笔
    private Paint mSubPaint;//副标题画笔
    private Paint mContentPaint;//内容画笔
    private Paint mDiscountPaint;//折扣画笔
    private int mTableColor = Color.parseColor("#cccccc");
    private int mTitleColor = Color.parseColor("#514f4f");
    private int msubColor = Color.parseColor("#666666");
    private int mContentColor = Color.parseColor("#333333");
    private int mDiscountColor = Color.parseColor("#ff8a01");
    private int mInnerMargin;//dp
    private String[] title1 = new String[]{"特权一", "特权二", "特权三", "特权四"};
    private String[] privilege1 = new String[]{"一元1分", "一元2分", "一元3分", "一元5分"};
    private String[] privilege2 = new String[]{"无", "体检产品", "体检产品", "体检产品"};
    private String[] privilege2_1 = new String[]{"9折", "8.5折", "8折"};

    public VipPrivilegeTableView(Context context) {
        this(context, null);
    }

    public VipPrivilegeTableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VipPrivilegeTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

        initData();

    }

    private void initData() {

        mInnerMargin = dp2px(13);
    }

    private int dp2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, getResources().getDisplayMetrics());
    }

    private void initPaint() {
        mTablePaint = new Paint();
        mTablePaint.setAntiAlias(true);
        mTablePaint.setColor(mTableColor);

        mTitilePaint = new Paint();
        mTitilePaint.setAntiAlias(true);
        mTitilePaint.setColor(mTitleColor);
        mTitilePaint.setTextSize(sp2px(14));

        mSmallTitilePaint = new Paint();
        mSmallTitilePaint.setAntiAlias(true);
        mSmallTitilePaint.setColor(mTitleColor);
        mSmallTitilePaint.setTextSize(sp2px(13));

        mSubPaint = new Paint();
        mSubPaint.setAntiAlias(true);
        mSubPaint.setColor(msubColor);
        mSubPaint.setTextSize(sp2px(12));

        mContentPaint = new Paint();
        mContentPaint.setAntiAlias(true);
        mContentPaint.setColor(mContentColor);
        mContentPaint.setTextSize(sp2px(12));

        mDiscountPaint = new Paint();
        mDiscountPaint.setAntiAlias(true);
        mDiscountPaint.setColor(mDiscountColor);
        mDiscountPaint.setTextSize(sp2px(14));


    }

    private float sp2px(int i) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, i, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        widgetLength = Math.min(measureHeight, measureWidth);
        setMeasuredDimension(widgetLength, widgetLength);//取最小边即屏幕宽度为控件边长
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        tableLength = widgetLength - 2 * mInnerMargin;
        //方格宽度
        float cubeLength = tableLength / 5;

        //画表格
        for (int i = 0; i < 6; i++) {
            //画竖线
            canvas.drawLine(mInnerMargin + i * cubeLength, mInnerMargin, mInnerMargin + i * cubeLength, mInnerMargin + tableLength, mTablePaint);
            //画横线
            canvas.drawLine(mInnerMargin, mInnerMargin + i * cubeLength, mInnerMargin + tableLength, mInnerMargin + i * cubeLength, mTablePaint);
        }
        //画斜线
        canvas.drawLine(mInnerMargin, mInnerMargin, mInnerMargin + cubeLength, mInnerMargin + cubeLength, mTablePaint);
        //画文字
        //小分类标题
        canvas.drawText("等级", mInnerMargin + cubeLength / 2, mInnerMargin + cubeLength / 3, mSmallTitilePaint);
        canvas.drawText("特权", mInnerMargin + cubeLength / 6, mInnerMargin + cubeLength * 4 / 5, mSmallTitilePaint);
        //上面标题
        canvas.drawText("普通会员", mInnerMargin + cubeLength * (1f + 1f / 8), mInnerMargin + cubeLength * 3 / 8, mTitilePaint);
        canvas.drawText("金卡会员", mInnerMargin + cubeLength * (2f + 1f / 8), mInnerMargin + cubeLength * 3 / 8, mTitilePaint);
        canvas.drawText("白金会员", mInnerMargin + cubeLength * (3f + 1f / 8), mInnerMargin + cubeLength * 3 / 8, mTitilePaint);
        canvas.drawText("钻石会员", mInnerMargin + cubeLength * (4f + 1f / 8), mInnerMargin + cubeLength * 3 / 8, mTitilePaint);
        //上面副标题
        canvas.drawText("手机注册", mInnerMargin + cubeLength * (1f + 1f / 6), mInnerMargin + cubeLength * 5 / 8, mSubPaint);
        canvas.drawText("单次充值", mInnerMargin + cubeLength * (2f + 1f / 6), mInnerMargin + cubeLength * 5 / 8, mSubPaint);
        canvas.drawText("单次充值", mInnerMargin + cubeLength * (3f + 1f / 6), mInnerMargin + cubeLength * 5 / 8, mSubPaint);
        canvas.drawText("单次充值", mInnerMargin + cubeLength * (4f + 1f / 6), mInnerMargin + cubeLength * 5 / 8, mSubPaint);

        canvas.drawText("即会员", mInnerMargin + cubeLength * (1f + 1f / 4), mInnerMargin + cubeLength * 7 / 8, mSubPaint);
        canvas.drawText("500元", mInnerMargin + cubeLength * (2f + 1f / 3), mInnerMargin + cubeLength * 7 / 8, mSubPaint);
        canvas.drawText("1000元", mInnerMargin + cubeLength * (3f + 1f / 5), mInnerMargin + cubeLength * 7 / 8, mSubPaint);
        canvas.drawText("2000元", mInnerMargin + cubeLength * (4f + 1f / 5), mInnerMargin + cubeLength * 7 / 8, mSubPaint);

        for (int i = 0; i < title1.length; i++) {
            //获取文字宽度，使居中
            Rect bounds = new Rect();
            mTitilePaint.getTextBounds(title1[i], 0, title1[i].length(), bounds);
            int textWidth = bounds.width();
            if (i == 0 || i == 3) {
                canvas.drawText(title1[i], mInnerMargin + cubeLength / 2 - textWidth / 2, mInnerMargin + cubeLength * (1 + i + 2f / 5), mTitilePaint);
            } else {
                canvas.drawText(title1[i], mInnerMargin + cubeLength / 2 - textWidth / 2, mInnerMargin + cubeLength * (1 + i + 1f / 3), mTitilePaint);
            }
        }

        String content1 = "返积分加速";
        Rect bounds1 = new Rect();
        mSubPaint.getTextBounds(content1, 0, content1.length(), bounds1);
        int textWidth1 = bounds1.width();
        canvas.drawText(content1, mInnerMargin + cubeLength / 2 - textWidth1 / 2, mInnerMargin + cubeLength * (1 + 3f / 4), mSubPaint);

        String content2 = "健康好礼";
        Rect bounds2 = new Rect();
        mSubPaint.getTextBounds(content2, 0, content2.length(), bounds2);
        int textWidth2 = bounds2.width();
        canvas.drawText(content2, mInnerMargin + cubeLength / 2 - textWidth2 / 2, mInnerMargin + cubeLength * (4 + 3f / 4), mSubPaint);

        String content3 = "指定产品";
        Rect bounds3 = new Rect();
        mSubPaint.getTextBounds(content3, 0, content3.length(), bounds3);
        int textWidth3 = bounds3.width();
        canvas.drawText(content3, mInnerMargin + cubeLength / 2 - textWidth3 / 2, mInnerMargin + cubeLength * (2 + 7f / 11), mSubPaint);

        String content4 = "专享优惠";
        Rect bounds4 = new Rect();
        mSubPaint.getTextBounds(content4, 0, content4.length(), bounds4);
        int textWidth4 = bounds4.width();
        canvas.drawText(content4, mInnerMargin + cubeLength / 2 - textWidth4 / 2, mInnerMargin + cubeLength * (2 + 6f / 7), mSubPaint);

        String content5 = "首次入会";
        Rect bounds5 = new Rect();
        mSubPaint.getTextBounds(content5, 0, content5.length(), bounds5);
        int textWidth5 = bounds5.width();
        canvas.drawText(content5, mInnerMargin + cubeLength / 2 - textWidth5 / 2, mInnerMargin + cubeLength * (3 + 7f / 11), mSubPaint);

        String content6 = "礼品";
        Rect bounds6 = new Rect();
        mSubPaint.getTextBounds(content6, 0, content6.length(), bounds6);
        int textWidth6 = bounds6.width();
        canvas.drawText(content6, mInnerMargin + cubeLength / 2 - textWidth6 / 2, mInnerMargin + cubeLength * (3 + 6f / 7), mSubPaint);

        for (int i = 0; i < privilege1.length; i++) {
            //获取baseline属性值

            Paint.FontMetricsInt fontMetricsInt = mContentPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
            float baseline = mInnerMargin + cubeLength * (1 + 1f / 2) + dy;

            Rect bounds7 = new Rect();
            mContentPaint.getTextBounds(privilege1[i], 0, privilege1[i].length(), bounds7);
            int textWidth7 = bounds7.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText(privilege1[i], mInnerMargin+cubeLength*(1+i+1f/2)-textWidth7/2, baseline, mContentPaint);//绘制文字
        }
        
        for (int i = 0; i < privilege2.length; i++) {
            //获取baseline属性值
            Paint.FontMetricsInt fontMetricsInt = mContentPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
            float baseline = mInnerMargin + cubeLength * (2 + 1f / 2) + dy;

            Rect bounds7 = new Rect();
            mContentPaint.getTextBounds(privilege2[i], 0, privilege2[i].length(), bounds7);
            int textWidth7 = bounds7.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText(privilege2[i], mInnerMargin+cubeLength*(1+i+1f/2)-textWidth7/2, baseline, mContentPaint);//绘制文字
        }

        for (int i = 0; i < 3; i++) {
            //获取baseline属性值
            Paint.FontMetricsInt fontMetricsInt = mContentPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
            float baseline = mInnerMargin + cubeLength * (2 + 1f / 4) + dy;

            Rect bounds7 = new Rect();
            mContentPaint.getTextBounds("全场", 0, "全场".length(), bounds7);
            int textWidth7 = bounds7.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText("全场", mInnerMargin+cubeLength*(2+i+1f/2)-textWidth7/2, baseline, mContentPaint);//绘制文字
        }
        for (int i = 0; i < privilege2_1.length; i++) {
            //获取baseline属性值
            Paint.FontMetricsInt fontMetricsInt = mDiscountPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
            float baseline = mInnerMargin + cubeLength * (2 + 3f / 4) + dy;

            Rect bounds7 = new Rect();
            mDiscountPaint.getTextBounds(privilege2_1[i], 0, privilege2_1[i].length(), bounds7);
            int textWidth7 = bounds7.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText(privilege2_1[i], mInnerMargin+cubeLength*(2+i+1f/2)-textWidth7/2, baseline, mDiscountPaint);//绘制文字
        }

        //获取baseline属性值
        Paint.FontMetricsInt fontMetricsInt = mContentPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
        float baseline = mInnerMargin + cubeLength * (3 + 1f / 2) + dy;

        Rect bounds7 = new Rect();
        mContentPaint.getTextBounds("无", 0, "无".length(), bounds7);
        int textWidth7 = bounds7.width();
        canvas.drawText("无", mInnerMargin+cubeLength*(1+1f/2)-textWidth7/2, baseline, mContentPaint);//绘制文字

        Rect bounds8 = new Rect();
        mContentPaint.getTextBounds("福碗", 0, "福碗".length(), bounds8);
        int textWidth8 = bounds8.width();
        canvas.drawText("福碗", mInnerMargin+cubeLength*(2+1f/2)-textWidth8/2, baseline, mContentPaint);//绘制文字

        for(int i = 0; i < 2; i++) {
            //获取baseline属性值
            Paint.FontMetricsInt fontMetricsInt1 = mContentPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
            int dy1 = (fontMetricsInt1.bottom - fontMetricsInt1.top) / 2 - fontMetricsInt1.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
            float baseline1 = mInnerMargin + cubeLength * (3 + 3f / 8) + dy1;

            Rect bounds9 = new Rect();
            mContentPaint.getTextBounds("礼品+219", 0, "礼品+219".length(), bounds9);
            int textWidth9 = bounds9.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText("礼品+219", mInnerMargin+cubeLength*(3+i+1f/2)-textWidth9/2, baseline1, mContentPaint);//绘制文字
            Rect bounds10 = new Rect();
            mContentPaint.getTextBounds("体检券一张", 0, "体检券一张".length(), bounds10);
            int textWidth10 = bounds10.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText("体检券一张", mInnerMargin+cubeLength*(3+i+1f/2)-textWidth10/2, baseline1+1f/4*cubeLength, mContentPaint);//绘制文字
        }

        //获取baseline属性值
        Paint.FontMetricsInt fontMetricsInt2 = mContentPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
        int dy2 = (fontMetricsInt2.bottom - fontMetricsInt2.top) / 2 - fontMetricsInt2.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
        float baseline2 = mInnerMargin + cubeLength * (4 + 1f / 2) + dy2;

        Rect bounds11 = new Rect();
        mContentPaint.getTextBounds("无", 0, "无".length(), bounds11);
        int textWidth11 = bounds11.width();
        canvas.drawText("无", mInnerMargin+cubeLength*(1+1f/2)-textWidth11/2, baseline2, mContentPaint);//绘制文字
        
        for(int i = 0; i < 3; i++) {
            //获取baseline属性值
            Paint.FontMetricsInt fontMetricsInt1 = mContentPaint.getFontMetricsInt();//获得的是文字的 坐标轴在baseline上。getTop();//获得的是控件的 坐标轴在控件左上角
            int dy1 = (fontMetricsInt1.bottom - fontMetricsInt1.top) / 2 - fontMetricsInt1.bottom;//控件中心线到baseline的距离，有关baseline属性：http://blog.csdn.net/xude1985/article/details/51532949
            float baseline1 = mInnerMargin + cubeLength * (4 + 3f / 8) + dy1;

            Rect bounds9 = new Rect();
            mContentPaint.getTextBounds("24小时健康", 0, "24小时健康".length(), bounds9);
            int textWidth9 = bounds9.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText("24小时健康", mInnerMargin+cubeLength*(2+i+1f/2)-textWidth9/2, baseline1, mContentPaint);//绘制文字
            Rect bounds10 = new Rect();
            mContentPaint.getTextBounds("服务/一年", 0, "服务/一年".length(), bounds10);
            int textWidth10 = bounds10.width();
            //text,x,y,paint x:文字起始横坐标  y:文字起始纵坐标baseline
            canvas.drawText("服务/一年", mInnerMargin+cubeLength*(2+i+1f/2)-textWidth10/2, baseline1+1f/4*cubeLength, mContentPaint);//绘制文字
        }
    }
}
