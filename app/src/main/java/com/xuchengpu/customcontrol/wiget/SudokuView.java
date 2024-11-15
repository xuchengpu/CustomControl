package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xuchengpu.customcontrol.bean.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许成谱 on 2018/3/29 15:12.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class SudokuView extends View {
    private Point[][] points;//用来存储九个宫格的圆心坐标信息
    private float outRadius;//宫格的外圆半径
    private float innerRadius;//宫格的内圆半径
    private Paint normalPaint;//正常情况下的画笔
    private Paint pressedPaint;//手指按下情况下的画笔
    private Paint errorPaint;//出错情况下的画笔
    private boolean isFirst = true;//第一次进入的时候才初始化
    private List<Point> selectPoints;//用来装选中的宫格圆心点相关信息
    //画最后一个选择点到手指间直线的起始坐标点
    private float lastX;
    private float lastY;
    //密码输入错误可重试次数
    private int tryCount = 5;
    //画最后一个选择点到手指间直线的画笔
    private Paint lastPaint;

    private String passWord = "12587";


    public SudokuView(Context context) {
        this(context, null);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        selectPoints = new ArrayList<>();

    }

    private void initPaint() {
        normalPaint = new Paint();
        normalPaint.setColor(Color.GRAY);
        normalPaint.setAntiAlias(true);
        normalPaint.setStrokeWidth(dp2px(2));
        normalPaint.setStyle(Paint.Style.STROKE);

        pressedPaint = new Paint();
        pressedPaint.setColor(Color.BLUE);
        pressedPaint.setAntiAlias(true);
        pressedPaint.setStrokeWidth(dp2px(2));
        pressedPaint.setStyle(Paint.Style.STROKE);

        errorPaint = new Paint();
        errorPaint.setColor(Color.RED);
        errorPaint.setAntiAlias(true);
        errorPaint.setStrokeWidth(dp2px(2));
        errorPaint.setStyle(Paint.Style.STROKE);


    }

    private float dp2px(int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, getResources().getDisplayMetrics());
    }

    private void initPointData() {

        points = new Point[3][3];

        int offsetY = 0;
        int offsetX = 0;
        int height = getHeight();
        int width = getWidth();
        //判断横竖屏
        if (height > width) {
            //竖屏
            offsetY = (height - width) / 2;
        } else {
            //横屏
            offsetX = (width - height) / 2;
            width = height;
        }

        float gridLength = width / 3;

        outRadius = gridLength / 4;
        innerRadius = outRadius / 4;

        //向数组中添加九个宫格圆心的坐标信息

        int index = 0;//用来记录是哪个宫格
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                index++;
                points[i][j] = new Point(offsetX + gridLength / 2 * (i * 2 + 1), offsetY + gridLength / 2 * (j * 2 + 1), index);
            }
        }
        lastPaint = pressedPaint;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //第一次进入时初始化数据
        if (isFirst) {
            initPointData();
            isFirst = false;
        }

        //画宫格
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if (point.getStatus() == Point.PointStatus.normal) {
                    //画外圆
                    canvas.drawCircle(point.getX(), point.getY(), outRadius, normalPaint);
                    //画内圆
                    canvas.drawCircle(point.getX(), point.getY(), innerRadius, normalPaint);
                }
                if (point.getStatus() == Point.PointStatus.pressed) {
                    //画外圆
                    canvas.drawCircle(point.getX(), point.getY(), outRadius, pressedPaint);
                    //画内圆
                    canvas.drawCircle(point.getX(), point.getY(), innerRadius, pressedPaint);
                }
                if (point.getStatus() == Point.PointStatus.error) {
                    //画外圆
                    canvas.drawCircle(point.getX(), point.getY(), outRadius, errorPaint);
                    //画内圆
                    canvas.drawCircle(point.getX(), point.getY(), innerRadius, errorPaint);
                }

            }
        }

        //画两个九宫格之间的直线
        if ( selectPoints==null||selectPoints.size() <= 0) {
            return;
        }

        Point primaryPoint = selectPoints.get(0);
        lastY = (float) (innerRadius * (y - primaryPoint.getY()) / Math.sqrt(Math.pow(y - primaryPoint.getY(), 2) + Math.pow(x - primaryPoint.getX(), 2)) + primaryPoint.getY());
        lastX = (float) (innerRadius * (x - primaryPoint.getX()) / Math.sqrt(Math.pow(y - primaryPoint.getY(), 2) + Math.pow(x - primaryPoint.getX(), 2)) + primaryPoint.getX());


        for (int i = 0; i < selectPoints.size(); i++) {
            Point startPoint = selectPoints.get(i);

            if (i + 1 == selectPoints.size()) {
                break;//结束for循环,防止数组下表越界
            }
            Point endPoint = selectPoints.get(i + 1);
            //此处几个坐标，后期可尝试提取为一个公共方法
            //起始点坐标
            float startY = (float) (innerRadius * (endPoint.getY() - startPoint.getY()) / Math.sqrt(Math.pow(endPoint.getY() - startPoint.getY(), 2) + Math.pow(endPoint.getX() - startPoint.getX(), 2)) + startPoint.getY());
            float startX = (float) (innerRadius * (endPoint.getX() - startPoint.getX()) / Math.sqrt(Math.pow(endPoint.getY() - startPoint.getY(), 2) + Math.pow(endPoint.getX() - startPoint.getX(), 2)) + startPoint.getX());
            //结束点坐标
            float endX = (float) (endPoint.getX() - innerRadius * (endPoint.getX() - startPoint.getX()) / Math.sqrt(Math.pow(endPoint.getY() - startPoint.getY(), 2) + Math.pow(endPoint.getX() - startPoint.getX(), 2)));
            float endY = (float) (endPoint.getY() - innerRadius * (endPoint.getY() - startPoint.getY()) / Math.sqrt(Math.pow(endPoint.getY() - startPoint.getY(), 2) + Math.pow(endPoint.getX() - startPoint.getX(), 2)));

            lastY = (float) (innerRadius * (y - endPoint.getY()) / Math.sqrt(Math.pow(y - endPoint.getY(), 2) + Math.pow(x - endPoint.getX(), 2)) + endPoint.getY());
            lastX = (float) (innerRadius * (x - endPoint.getX()) / Math.sqrt(Math.pow(y - endPoint.getY(), 2) + Math.pow(x - endPoint.getX(), 2)) + endPoint.getX());
            canvas.drawLine(startX, startY, endX, endY, lastPaint);

        }

        //画最终点到手指间的直线
        canvas.drawLine(lastX, lastY, x, y, lastPaint);


    }

    private float x;//手指在屏幕上的X坐标
    private float y;//手指在屏幕上的Y坐标
    private boolean isOnTouch = false;//当手指第一次不是按在九个宫格内部时，屏蔽掉后续的move与up事件
    private boolean isPermit=true;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point downPoint = checkInCircle(x, y, outRadius);
                if (downPoint != null) {
                    isOnTouch = true;
                    isPermit=false;

                    toPrimaryState();//先清除上一次数据的影响

                    downPoint.setStatus(Point.PointStatus.pressed);
                    selectPoints.add(downPoint);
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (isOnTouch) {
                    Point movePoint = checkInCircle(x, y, outRadius);
                    if (movePoint != null) {
                        movePoint.setStatus(Point.PointStatus.pressed);

                        try {
                            if (!selectPoints.get(selectPoints.size() - 1).equals(movePoint)) {//防止同一个宫格连续重复添加
                                selectPoints.add(movePoint);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                isOnTouch = false;
                isPermit=true;//允许清空数据
                //手指抬起后最后宫格到手指间的距离置零（即让起始点==终点）
                x = lastX;
                y = lastY;
                //回调，校验密码
                StringBuilder code = new StringBuilder();
                for (int i = 0; i < selectPoints.size(); i++) {
                    int index = selectPoints.get(i).getIndex();
                    code.append(index);
                }
                Toast.makeText(getContext(), "code==" + code.toString(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "code==" + code.toString());
                if (passWord.equals(code.toString())) {
                    //密码相符，跳转到目标页面,数据清零
                    toPrimaryState();
                    tryCount = 5;
                    Toast.makeText(getContext(), "密码正确！", Toast.LENGTH_SHORT).show();
                } else {
                    //密码不正确，置红
                    for (int i = 0; i < selectPoints.size(); i++) {
                        selectPoints.get(i).setStatus(Point.PointStatus.error);
                        lastPaint = errorPaint;
                    }
                    //延迟清空数据
                   postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isPermit) {
                                toPrimaryState();
                                tryCount--;
                                Toast.makeText(getContext(), "您还剩余" + tryCount + "次重新输入密码", Toast.LENGTH_SHORT).show();
                                postInvalidate();
                            }
                        }
                    }, 1000);
                }
                break;
        }

        invalidate();
        return true;
    }

    /**
     * 恢复的原始状态
     */
    private void toPrimaryState() {
        selectPoints.clear();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                point.setStatus(Point.PointStatus.normal);
            }
        }
        lastPaint = pressedPaint;
    }

    /**
     * 检查是否在圆圈内
     * @param x
     * @param y
     * @param radius
     * @return
     */
    private Point checkInCircle(float x, float y, float radius) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                float length = (float) Math.sqrt(Math.pow(x - point.getX(), 2) + Math.pow(y - point.getY(), 2));
                if (length < radius) {
                    return point;
                }
            }
        }

        return null;
    }
}
