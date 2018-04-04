package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许成谱 on 2018/4/4 17:05.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class Flowlayout extends ViewGroup {
    private List<List<View>> groups;//用来装一行一行的集合

    public Flowlayout(Context context) {
        this(context, null);
    }

    public Flowlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Flowlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        groups = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int maxHeight = 0;

        groups.clear();//因为onmeasure方法不止调用一次
        List<View> lineViews = new ArrayList<>();//用来装每行的子view的集合


        int lineWidth = 0;
        //1、for循环测量子view
        int childCount = getChildCount();//获取子view数量
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);//获取子view
            measureChild(child, widthMeasureSpec, heightMeasureSpec);//此处测量后就可以获取子view的宽高

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            lineWidth += childWidth;//若干子view累积的宽度

            if (lineWidth > width) {//累积的宽度与父布局的宽度比较
                //超过父布局的宽度时
                height += maxHeight;//每换一行就加上上一行的高度
                maxHeight = 0;//上一行的高度清零
                groups.add(lineViews);//将每行的集合添加进装行数的总组中
                lineViews = new ArrayList<>();//新的一行就重新new一个集合来装一行的子view
                lineViews.add(child);//将每行的子view添加进每行的集合中
                lineWidth = childWidth;//此处为另起一行，需要重置这一行的宽度为此行第一个子view的宽度
            } else {
                //不超过父布局的宽度

                maxHeight = Math.max(maxHeight, childHeight);//取每行最大的高度值
                lineViews.add(child);//将每行的子view添加进每行的集合中
            }


        }
        //2、根据子view计算和指定自己的布局
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //3、循环遍历给每个子view指定位置
        int lineHeight = 0;
        for (List<View> group : groups) {
            int left = 0;
            for (View child : group) {
                child.layout(left, lineHeight, left+child.getMeasuredWidth(), lineHeight+child.getMeasuredHeight());
                left += child.getMeasuredWidth();
            }
            lineHeight += group.get(0).getHeight();
        }
    }
}
