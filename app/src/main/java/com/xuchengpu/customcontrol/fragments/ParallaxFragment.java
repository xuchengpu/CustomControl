package com.xuchengpu.customcontrol.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.utils.AppCompatViewInflater;
import com.xuchengpu.customcontrol.utils.ParallaxTag;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许成谱 on 2018/5/17 16:41.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class ParallaxFragment extends Fragment implements LayoutInflaterFactory {
    public static final String LAYOUT = "LAYOUT";
    private AppCompatViewInflater mAppCompatViewInflater;//参照源码拿来的
    private List<View> views = new ArrayList<>();//存放所有设置了视差动画的view
    private int[] mParallaxAttrs = new int[]{
            R.attr.translationXIn,
            R.attr.translationXOut, R.attr.translationYIn, R.attr.translationYOut};//自定义的几个属性

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int layoutId = bundle.getInt(LAYOUT);

        // 把所有需要移动的属性解析出来，内涵端子插件式换肤
        // View创建的时候 我们去解析属性  这里传 inflater 有没有问题？ 单例设计模式 代表着所有的View的创建都会是该 Fragment 去创建的
        inflater = inflater.cloneInContext(getContext());
        LayoutInflaterCompat.setFactory(inflater, this);//设置后会导致调用方法1
        return inflater.inflate(layoutId, container, false);
    }

    //方法1  此处拦截view的创建，创建过程交给自己来完成
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 1. 创建View  参照源码拷贝过来的
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);
        if (view != null) {
            //2、解析
            analysisAttrs(context, attrs, view);
        }
        return view;
    }

    private void analysisAttrs(Context context, AttributeSet attrs, View view) {
        //获取自定义属性的另外一种方式，参照TextView的源码
        TypedArray array = context.obtainStyledAttributes(attrs, mParallaxAttrs);
        //参照TextView的源码这么写
        if (array != null && array.getIndexCount() != 0) {
            int indexCount = array.getIndexCount();//获取属性数
            ParallaxTag tag = new ParallaxTag();
            for (int i = 0; i < indexCount; i++) {
                int index = array.getIndex(i);//获取的是attr在mParallaxAttrs中的序引号 0,1,2,3
                switch (index) {
                    case 0:
                        tag.translationXIn = array.getFloat(0, 0);
                        break;
                    case 1:
                        tag.translationXOut = array.getFloat(1, 0);
                        break;
                    case 2:
                        tag.translationYIn = array.getFloat(2, 0);
                        break;
                    case 3:
                        tag.translationYOut = array.getFloat(3, 0);
                        break;
                }
            }
            // 自定义属性怎么存? 还要一一绑定  在View上面设置一个tag
            view.setTag(R.id.parallax_tag, tag);
//            Log.e("TAG", tag.toString());
            views.add(view);//添加到集合中
        }
    }

    @SuppressLint("RestrictedApi")//从源码直接拷贝过来就行了
    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new AppCompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (!(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    //给viewpager提供接口去拿views,在viewpager中根据view的tag对view设置视差动画
    public List<View> getParallaxViews() {
        return views;
    }
}
