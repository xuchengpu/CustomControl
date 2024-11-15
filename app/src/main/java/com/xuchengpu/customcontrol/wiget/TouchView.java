package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by 许成谱 on 2018/4/6 18:31.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class TouchView extends View {
    public TouchView(Context context) {
        this(context,null);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "View.onTouchEvent-->"+event.getAction());
        return super.onTouchEvent(event);
//        return true;
//        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        super.dispatchTouchEvent(event);
//        return true;
        Log.e("TAG", "View.dispatchTouchEvent-->"+event.getAction());
        return super.dispatchTouchEvent(event);
    }
}
