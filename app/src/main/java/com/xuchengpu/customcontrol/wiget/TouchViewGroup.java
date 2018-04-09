package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by 许成谱 on 2018/4/8 13:41.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class TouchViewGroup extends LinearLayout {
    public TouchViewGroup(Context context) {
        this(context,null);
    }

    public TouchViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "ViewGroup.onTouchEvent-->"+event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("TAG", "ViewGroup.dispatchTouchEvent-->"+event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("TAG", "ViewGroup.onInterceptTouchEvent-->"+event.getAction());
        return super.onInterceptTouchEvent(event);
//        return  true;
    }
}
