package com.xuchengpu.customcontrol.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xuchengpu.customcontrol.adapter.MultipleBaseAdapter;

/**
 * Created by 许成谱 on 2018/5/5 13:08.
 * qq:1550540124
 * for:热爱生活每一天！
 * 仿58同城头部多菜单栏目筛选
 */

public class MultipleMenuSelectorView extends LinearLayout {
    private Context mContext;
    private LinearLayout mTabLayout;//头部tab布局
    private FrameLayout menuContainer;//底部菜单容器布局：用来存放=阴影（View） + 菜单内容布局(FrameLayout)
    private FrameLayout shadowView;//阴影
    private int shadowColor = 0x88888888;
    private FrameLayout menuLayout;//菜单内容布局
    private boolean isMenuOpen = false;//菜单栏是否打开
    private float menuLayoutHeight;//菜单内容布局的高度
    private int currentPosition = -1;//打开菜单的当前位置
    private boolean isAnimatorDoing = false;//动画正在执行
    private MultipleBaseAdapter mAdapter;

    public MultipleMenuSelectorView(Context context) {
        this(context, null);
    }

    public MultipleMenuSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleMenuSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        //先设置添加布局的方向
        setOrientation(VERTICAL);
        //使用动态的方式去添加布局
        // 1.1 创建头部用来存放 Tab
        mTabLayout = new LinearLayout(mContext);
        mTabLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //添加进总布局父布局
        addView(mTabLayout);
        // 1.2 创建 FrameLayout 用来存放 = 阴影（View） + 菜单内容布局(FrameLayout)
        menuContainer = new FrameLayout(mContext);
        LinearLayout.LayoutParams menuContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menuContainer.setLayoutParams(menuContainerParams);
        //添加进总布局父布局
        addView(menuContainer);
        // 创建阴影 可以不用设置 LayoutParams 默认就是 MATCH_PARENT ，MATCH_PARENT
        shadowView = new FrameLayout(mContext);
        shadowView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        shadowView.setBackgroundColor(shadowColor);
        //刚开始的时候透明度是0
        shadowView.setAlpha(0);
        //添加进底部菜单栏容器布局
        menuContainer.addView(shadowView);
        // 创建菜单用来存放菜单内容
        menuLayout = new FrameLayout(mContext);
        menuLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //添加进底部菜单栏容器布局
        menuContainer.addView(menuLayout);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置容纳菜单的布局为整个布局的3/4高度  在测量之后才能设置，否则根本不知道父布局高度是多少
        if (currentPosition != -1) {
            return;//保证只有在第一次的时候下面的方法才会执行，否则会导致菜单的隐藏
        }
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        menuLayoutHeight = viewHeight * 3f / 4;
        ViewGroup.LayoutParams params = menuLayout.getLayoutParams();
        params.height = (int) menuLayoutHeight;
        menuLayout.setLayoutParams(params);
        menuLayout.setBackgroundColor(Color.WHITE);

        //刚开始是隐藏的,点击菜单的时候才会出现
        menuLayout.setTranslationY(-menuLayoutHeight);
    }

    /**
     * 设置适配器，把应该让外界实现的暴露出去，更灵活的去让别人实现自己的 布局
     */
    public void setAdapter(MultipleBaseAdapter adapter) {
        if (adapter == null) {
            Log.e("TAG", "多条目筛选适配器为null");
            return;
        }
        mAdapter = adapter;
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            //添加tab导航栏view
            final View tabView = mAdapter.getTabView(i, this);
            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            mTabLayout.addView(tabView);

            //设置点击监听
            final int finalI = i;
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //动画正在执行时，屏蔽掉连续点击事件
                    if (!isAnimatorDoing) {
                        //如果是打开就关闭，如果是关闭就打开
                        if (isMenuOpen) {
                            if (currentPosition != finalI) {
                                //菜单打开状态，点击的是不同的tab,说明此时是切换菜单
                                changeMenu(tabView, finalI);
                            } else {
                                //菜单打开状态，点击的是相同的tab,说明此时是关闭菜单
                                closeMenu();
                            }

                        } else {
                            openMenu(tabView, finalI);

                        }
                    }
                }
            });

            //添加菜单内容栏view
            View menuView = mAdapter.getMenuView(i, this);
            menuView.setVisibility(GONE);
            menuLayout.addView(menuView);

            //点击阴影 菜单关闭
            shadowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //屏蔽掉连续点击事件
                    if (!isAnimatorDoing) {
                        //点击阴影区域关闭菜单
                        if (isMenuOpen && isCliable) {
                            closeMenu();
                        }
                    }

                }
            });

        }
    }

    private boolean isCliable = false;//设置点击阴影部分的有效区域

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float y = event.getY();
                float tabHeiht = mTabLayout.getMeasuredHeight();
                if (y - tabHeiht > menuLayoutHeight) {
                    isCliable = true;
                } else {
                    isCliable = false;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void changeMenu(View view, int position) {
        //注意此处会导致onmeasure方法执行，所以要重新测量
        View sellectMenu = menuLayout.getChildAt(position);
        sellectMenu.setVisibility(VISIBLE);//切换显示选中的menu
        View previousMenu = menuLayout.getChildAt(currentPosition);
        previousMenu.setVisibility(GONE);
        //更新选中的tab字体颜色大小
        mAdapter.updateSelectedView(view, position);
        mAdapter.updatePreView(mTabLayout.getChildAt(currentPosition), currentPosition);
        currentPosition = position;
    }

    private void closeMenu() {
        //向上平移的动画
        ObjectAnimator translationY = ObjectAnimator.ofFloat(menuLayout, "translationY", 0, -menuLayoutHeight);
        translationY.setDuration(400);
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画执行完毕，隐藏所有菜单
                hideAll();
                shadowView.setVisibility(GONE);//菜单隐藏起来后，要把阴影部分影藏调，否则它作为蒙层会拦截点击事件
                isMenuOpen = false;
                isAnimatorDoing = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimatorDoing = true;
            }
        });
        translationY.start();
        //阴影的动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(shadowView, "alpha", 1f, 0f);
        alpha.setDuration(400);
        alpha.start();
        //更新被取消选中的tab字体颜色大小，比如致灰等
        mAdapter.updatePreView(mTabLayout.getChildAt(currentPosition), currentPosition);


    }

    /**
     * 隐藏所有的menu
     */
    private void hideAll() {
        int childCount = menuLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            menuLayout.getChildAt(i).setVisibility(GONE);
        }
    }

    private void openMenu(View tabView, int position) {
        currentPosition = position;
        //打开哪个就显示哪个
        View menu = menuLayout.getChildAt(position);
        if (menu == null) {
            return;
        }
        menu.setVisibility(VISIBLE);
        shadowView.setVisibility(VISIBLE);
        //向下平移的动画
        ObjectAnimator translationY = ObjectAnimator.ofFloat(menuLayout, "translationY", -menuLayoutHeight, 0);
        translationY.setDuration(400);
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isMenuOpen = true;
                isAnimatorDoing = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimatorDoing = true;
            }
        });
        translationY.start();
        //阴影的动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(shadowView, "alpha", 0f, 1f);
        alpha.setDuration(400);
        alpha.start();
        //更新选中的tab字体颜色大小
        mAdapter.updateSelectedView(tabView, position);

    }
}
