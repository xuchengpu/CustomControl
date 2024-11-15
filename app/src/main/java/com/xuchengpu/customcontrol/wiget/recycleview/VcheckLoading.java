package com.xuchengpu.customcontrol.wiget.recycleview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.xuchengpu.customcontrol.R;


/**
 * Created by 许成谱 on 2017/10/23 18:04.
 * qq:1550540124
 * 热爱生活每一天！
 * for:页面预加载
 */

public class VcheckLoading extends RelativeLayout {

    private ImageView ivCircle;
    private AnimationDrawable rocketAnimation;

    public VcheckLoading(Context context) {
        this(context, null);
    }

    public VcheckLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.vcheck_loading, this);
        ivCircle = (ImageView) findViewById(R.id.iv_circle);
        rocketAnimation = (AnimationDrawable) ivCircle.getBackground();
        rocketAnimation.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}
