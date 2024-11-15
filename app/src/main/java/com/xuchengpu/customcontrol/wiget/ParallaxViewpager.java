package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.fragments.ParallaxFragment;
import com.xuchengpu.customcontrol.utils.ParallaxTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许成谱 on 2018/5/17 15:47.
 * qq:1550540124
 * 热爱生活每一天！
 * 让内部的fragment中的子控件根据xml文件指定的属性来实现位移动画
 * 使用方式：调用setData。对需要设置视差动画的view在布局xml中对目标view设置translationXIn,
 * translationXOut, translationYIn, translationYOut等几个属性
 */

public class ParallaxViewpager extends ViewPager {

    private List<ParallaxFragment> fragments;

    public ParallaxViewpager(Context context) {
        this(context, null);
    }

    public ParallaxViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        fragments = new ArrayList<>();
    }

    /**
     * 给viewpager设置数据的入口，一键调用。
     *
     * @param fm      fragment管理者
     * @param layouts 布局
     */
    public void setData(FragmentManager fm, int[] layouts) {
        //初始化fragment
        initFragments(layouts);
        //设置适配器
        setAdapter(new ParallaxFragmentPagerAdapter(fm));
        //设置监听 拿到fragment中的控件，并根据viewpager的位移来设置平移动画效果
        addOnPageChangeListener(new ParallaxViewpagerListener());
    }

    private void initFragments(int[] layouts) {
        for (int layout : layouts) {
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ParallaxFragment.LAYOUT, layout);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }

    class ParallaxFragmentPagerAdapter extends FragmentPagerAdapter {

        public ParallaxFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    class ParallaxViewpagerListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //向左滑动时 positon: 当前位置  positionOffset:0-->1  positionOffsetPixels:0-->measuredWidth
            try {
                ParallaxFragment currentFragment = fragments.get(position);
                List<View> parallaxViews = currentFragment.getParallaxViews();
                for (int i = 0; i < parallaxViews.size(); i++) {
                    View view = parallaxViews.get(i);
                    ParallaxTag tag = (ParallaxTag) view.getTag(R.id.parallax_tag);
                    view.setTranslationX(-positionOffsetPixels * tag.translationXOut);
                    view.setTranslationY(-positionOffsetPixels * tag.translationYOut);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ParallaxFragment currentFragment = fragments.get(position + 1);
                List<View> parallaxViews = currentFragment.getParallaxViews();
                for (int i = 0; i < parallaxViews.size(); i++) {
                    View view = parallaxViews.get(i);
                    ParallaxTag tag = (ParallaxTag) view.getTag(R.id.parallax_tag);
                    view.setTranslationX((getMeasuredWidth() - positionOffsetPixels) * tag.translationXIn);
                    view.setTranslationY((getMeasuredWidth() - positionOffsetPixels) * tag.translationYIn);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
