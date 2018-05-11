package com.xuchengpu.customcontrol.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.DragBeiSaiErAndBombView;

import static android.view.View.INVISIBLE;

/**
 * Created by 许成谱 on 2018/5/11 18:36.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class MyViewOnTouchListener implements View.OnTouchListener,DragBeiSaiErAndBombView.ViewListener {

    private final DragBeiSaiErAndBombView dragView;
    private final Context mContext;
    private final WindowManager windowManager;
    private final WindowManager.LayoutParams params;
    private final View originView;
    private Bitmap bitMapView;

    float startX;
    float startY;

    public MyViewOnTouchListener(View view, DragBeiSaiErAndBombView dragView, Context context) {
        this.originView = view;
        this.dragView = dragView;
        this.mContext = context;
        //把view添加到windowManager中，让贝瑟尔view可以在全屏滑动，否则只能在我们的根布局内滑动
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        //设置成背景透明
        params.format = PixelFormat.TRANSPARENT;
        dragView.setOnViewListener(this);

    }

    /**
     * 从一个View中获取Bitmap
     *
     * @param view
     * @return
     */
    private Bitmap getBitMapView(View view) {
        view.buildDrawingCache();
        Bitmap drawingCache = view.getDrawingCache();
        return drawingCache;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                windowManager.addView(dragView, params);

                originView.setVisibility(INVISIBLE);//点击就把原view隐藏起来

                //拿到一个view的bitmap对象
                bitMapView = getBitMapView(originView);
                dragView.setBitmap(bitMapView);

                //获取初始view的位置,避免手指一触碰到就抖动
                int[] location = new int[2];
                originView.getLocationOnScreen(location);
                //手指按下时初始化坐标
                startX = location[0] + bitMapView.getWidth() / 2;
                startY = location[1] + bitMapView.getHeight() / 2- BubbleUtils.getStatusBarHeight(mContext);

                dragView.initFixationPoint(startX, startY);
                dragView.initDragPoint(startX, startY);
                break;
            case MotionEvent.ACTION_MOVE:
                dragView.initDragPoint(event.getRawX(), event.getRawY()- BubbleUtils.getStatusBarHeight(mContext));
                break;
            case MotionEvent.ACTION_UP:
                dragView.handleActionUp();
                break;
        }
        dragView.invalidate();
        return true;
    }

    @Override
    public void restore() {

    }

    @Override
    public void dismiss(Point point) {
        executeBombAnimator(point);
    }

    /**
     * 执行爆炸动画
     */
    private void executeBombAnimator(Point point) {
        //先像window中添加一个framelayout,再在framlayout中添加子view imageview
        final FrameLayout frameLayout=new FrameLayout(mContext);
        final ImageView imageView = new ImageView(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
        frameLayout.addView(imageView);
        windowManager.removeView(dragView);//先移除显示贝塞尔曲线及影像的bitmap所在的dragview
        windowManager.addView(frameLayout,this.params);//再在window里添加爆炸的view
        imageView.setX(point.x);
        imageView.setY(point.y);
        //执行爆炸帧动画
        imageView.setBackgroundResource(R.drawable.anim_bomb);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();

        //动画结束从window里移除view
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                windowManager.removeView(frameLayout);
                //通知一下activity

            }
        },getDrawableTime(animationDrawable));

    }

    private long getDrawableTime(AnimationDrawable drawable) {
        int frameCount = drawable.getNumberOfFrames();
        int totalTime=0;
        for(int i = 0; i < frameCount; i++) {
            totalTime+=drawable.getDuration(i);
        }
        return totalTime;
    }

}