package com.xuchengpu.customcontrol.activitys;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.base.BaseFragment;
import com.xuchengpu.customcontrol.fragments.ItemFragment;
import com.xuchengpu.customcontrol.wiget.DisColorTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class IndicatorActivity extends AppCompatActivity {

    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.dc_tv)
    DisColorTextView dcTv;
    @BindView(R.id.btn_left_to_right)
    Button btnLeftToRight;
    @BindView(R.id.btn_right_to_left)
    Button btnRightToLeft;
    private String[] titles = {"直播", "推荐", "视频", "图片", "段子", "精华"};
    private ArrayList<DisColorTextView> indicators = new ArrayList<>();
    private ArrayList<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        ButterKnife.bind(this);

        initData();
        initViewPager();

    }

    private void initViewPager() {
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new MyListener());

    }

    class MyListener implements ViewPager.OnPageChangeListener {
        private int position;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            this.position = position;
            DisColorTextView view = indicators.get(position);
            view.setOritation(DisColorTextView.Oritation.LEFT_TO_RIGHT);
            view.setProgress(positionOffset);

            try {
                DisColorTextView view2 = indicators.get(position + 1);
                view2.setOritation(DisColorTextView.Oritation.RIGHT_TO_LEFT);
                view2.setProgress(positionOffset);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("TAG", "position-->" + position + "positionOffset-->" + positionOffset);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            if (state == 2) {
//
//                for(int i = 0; i < indicators.size(); i++) {
//                    indicators.get(i).setMyTextSize(dp2px(14));
//                }
//                indicators.get(position).setMyTextSize(dp2px(28));
//            }
        }
    }

    private int dp2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,i,getResources().getDisplayMetrics());
    }

    class MyAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> fragments;

        public MyAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragments = fragments;
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

    private void initData() {
        fragments = new ArrayList<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, -2);
        params.weight = 1;
        for (int i = 0; i < titles.length; i++) {
            DisColorTextView dctv = new DisColorTextView(this);
            dctv.setText(titles[i]);
            dctv.setTextColor(Color.BLACK);
            dctv.setLayoutParams(params);
            llIndicator.addView(dctv);
            indicators.add(dctv);

            ItemFragment fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("title", titles[i]);
            fragment.setArguments(args);
            fragments.add(fragment);

        }


    }

    @OnClick({R.id.btn_left_to_right, R.id.btn_right_to_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left_to_right:
                move(DisColorTextView.Oritation.LEFT_TO_RIGHT);
                break;
            case R.id.btn_right_to_left:
                move(DisColorTextView.Oritation.RIGHT_TO_LEFT);
                break;
        }
    }

    private void move(final DisColorTextView.Oritation oritation) {
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                dcTv.setOritation(oritation);
                dcTv.setProgress(progress);
            }
        });
        animator.start();

    }
}
