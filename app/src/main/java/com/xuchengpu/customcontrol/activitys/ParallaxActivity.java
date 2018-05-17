package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.ParallaxViewpager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ParallaxActivity extends AppCompatActivity {

    @InjectView(R.id.pl_viewpager)
    ParallaxViewpager plViewpager;
    private int layouts [] =new int[]{R.layout.fragment_page_first,R.layout.fragment_page_second,R.layout.fragment_page_third};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax);
        ButterKnife.inject(this);
        plViewpager.setData(getSupportFragmentManager(),layouts);//封装的思想，方便调用者调用，具体的繁琐工作交给ParallaxViewpager内部去完成
    }
}
