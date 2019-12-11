package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import com.xuchengpu.customcontrol.R;


/**
 * Created by 许成谱 on 2018/4/9 10:32.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class KuGouView extends HorizontalScrollView {

    private int menuRightMargin;//自定义属性，用来设置菜单栏的宽度
    private View menuView;//菜单栏view
    private ViewGroup contentView;//主页内容view
    private GestureDetector gestureDetector;//手势识别器，用于处理快速滑动
    private boolean menuIsOpen = false;//用于判断菜单是否关闭
    private boolean isIntercept;
    private ViewGroup.LayoutParams contentParams;
    private View view;


    public KuGouView(Context context) {
        this(context, null);
    }

    public KuGouView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KuGouView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KuGouView);
        menuRightMargin = array.getDimensionPixelSize(R.styleable.KuGouView_MenuRightMargin, dp2px(20));
        array.recycle();
        gestureDetector = new GestureDetector(context, new MyGestureListener());//第一步
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    //1、布局错乱。由于在xml中指定的是match等，没有确切的值，此处在布局实例加载完毕后需要指定宽度，方便后边的 super.onLayout(changed, l, t, r, b)布置

    /**
     * 该方法在布局实例加载完成(xml解析完毕)时回调，在onlayout()之前.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);//得到HorizontalScrollView的直接子孩子linearlayout
        int childCount = viewGroup.getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("只能放置两个子布局");
        }
        //宽度设置给menuView
        menuView = viewGroup.getChildAt(0);//菜单栏view
        ViewGroup.LayoutParams menuParams = menuView.getLayoutParams();
        menuParams.width = getScreenWidth() - menuRightMargin;
        menuView.setLayoutParams(menuParams);//7.0以上手机必须要加这个方法
        //宽度设置给contentView


        //此处添加QQ6.0侧滑contentview阴影效果
        //思路：方式一、先把view从它原来的父容器中提取出来，然后包裹上一个新的父容器，在这个父容器中添加一个新的子view，
        // 给子view设置透明度来实现阴影效果，然后把这个父容器再添加进原来的父容器中
//        contentView =  viewGroup.getChildAt(1);//内容栏view
//        viewGroup.removeView(contentView);
//        RelativeLayout container=new RelativeLayout(getContext());
//        container.addView(contentView);
//        View view=new View(getContext());
//        view.setBackgroundColor(Color.parseColor("#55000000"));
//        container.addView(view);
//
//        contentParams =  contentView.getLayoutParams();
//        contentParams.width=getScreenWidth();
//        container.setLayoutParams(contentParams);
//
//        viewGroup.addView(container);
        //方式二、直接添加一个子view
        contentView = (ViewGroup) viewGroup.getChildAt(1);//内容栏view
        contentParams = contentView.getLayoutParams();
        contentParams.width = getScreenWidth();
        contentView.setLayoutParams(contentParams);

        view = new View(getContext());
        view.setBackgroundColor(Color.parseColor("#55000000"));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(param);
        contentView.addView(view);

        contentView.getChildAt(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "你点击了TextView", Toast.LENGTH_SHORT).show();
            }
        });
        view.setAlpha(0f);

    }

    private int getScreenWidth() {//获取屏幕的参数
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //2、自动滚动到contentview
        scrollTo(getScreenWidth() - menuRightMargin, 0);//这个方法只能在 super.onLayout(changed, l, t, r, b);调用，否则系统会根据测量到的结果在super中给它恢复到初始的样子
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isIntercept = false;
        float x = ev.getX();
        if (x > (getScreenWidth() - menuRightMargin)) {//超出菜单栏宽度时需要拦截
            isIntercept = true;
            return true;//return 即拦截了事件不再给子类分发
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isIntercept) {//拦截后会将事件传递给本层的onTouchEvent，为避免冲突，此处需要拦截
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                //关闭菜单
                closeMenu();//优化一下，将此方法由onInterceptTouchEvent移到这里，可以使只在up事件执行时才进行关闭菜单操作
            }
            return true;
        }
        if (gestureDetector.onTouchEvent(ev)) {//第二步：将事件传递给gestureDetector 第五步：该返回值是从MyGestureListener.onFling()中返回来的
            return true;//第六步：事件已经传递给了MyGestureListener.onFling(),已经执行打开或者关闭菜单操作了。后边再执行或有可能up中执行相反操作，所以需要拦截
        }

        //3、处理手指抬起自动回弹效果
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (getScrollX() > menuView.getMeasuredWidth() / 2) {
                //关闭菜单
                closeMenu();
            } else {
                //打开菜单
                openMenu();
            }
            return true;//super里又发出了一个相反的命令，所以需要屏蔽
        }
        return super.onTouchEvent(ev);
    }

    private void openMenu() {
        smoothScrollTo(0, 0);//带有动画的移动，由父类提供
        menuIsOpen = true;
    }

    private void closeMenu() {
        smoothScrollTo(menuView.getMeasuredWidth(), 0);
        menuIsOpen = false;
    }
    //4、根据滑动距离，处理缩放，透明度等

    /**
     * 滑动的回调，由父类提供
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //求出缩放系数1-->0
        float scale = 1f * l / menuView.getMeasuredWidth();

        //contentview的缩放
        float rightScale = (float) (0.7f + scale * 0.3);
        contentView.setPivotX(0);//移动缩放中心点
        contentView.setPivotY(contentView.getMeasuredHeight() / 2);
        contentView.setScaleX(rightScale);//X轴缩放
        contentView.setScaleY(rightScale);

        //menuview的缩放
        // 透明度是 半透明到完全透明  0.5f - 1.0f
        float leftAlpha = 0.5f + (1 - scale) * 0.5f;
        // 缩放 0.7f - 1.0f
        float leftScale = 0.7f + (1 - scale) * 0.3f;
//        menuView.setScaleY(leftScale);
//        menuView.setScaleX(leftScale);

        menuView.setAlpha(leftAlpha);//设置透明度

        menuView.setTranslationX((float) (l*0.2 ));
//        menuView.setTranslationX((float) (l ));//设置抽屉式效果，平移view
//        menuView.setTranslationX((float) (-l ));//设置抽屉式效果，平移view
//        Log.e("TAG", "l=="+l);

        //添加仿qq6.0侧滑菜单阴影渐变效果
        view.setAlpha(1-scale);
    }

    //SimpleOnGestureListener是对OnGestureListener的一次包装，可简化我们需要实现的方法数，实际上从源码可以看到，它内部什么什么也没干，
    // 我们需要实现什么具体功能需要重写对应的方法，好处就是我们重写所有的方法了，简洁
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //velocityX 沿着X轴的速度
            Log.e("TAG", "velocityX==" + velocityX);
            //手指往右滑动为正值
            //手指往左滑动负值

            if (menuIsOpen) {//菜单栏是打开状态
                if (velocityX < 0) {//手指往左滑动velocityX<0  第三步：根据菜单关闭状态执行相应的滑动方向判断
                    closeMenu();//关闭菜单
                    return true;// 第四步：提供给gestureDetector.onTouchEvent(ev),作为他的返回值，在view的onTouchEvent中使用
                }

            } else {//菜单栏是关闭状态
                if (velocityX > 0) {//手指往右滑动，velocityX>0
                    openMenu();
                    return true;//提供给gestureDetector.onTouchEvent(ev),作为他的返回值，在view的onTouchEvent中使用
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
