package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.ParallaxViewpager;
import com.xuchengpu.customcontrol.wiget.YahooLoadingView;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class ParallaxActivity extends AppCompatActivity {

    @BindView(R.id.pl_viewpager)
    ParallaxViewpager plViewpager;
    @BindView(R.id.yahoo_view)
    YahooLoadingView yahooView;
    @BindView(R.id.btn_begin)
    Button btnBegin;
    @BindView(R.id.btn_stop)
    Button btnStop;
    private int layouts[] = new int[]{R.layout.fragment_page_first, R.layout.fragment_page_second, R.layout.fragment_page_third};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax);
        ButterKnife.bind(this);
        plViewpager.setData(getSupportFragmentManager(), layouts);//封装的思想，方便调用者调用，具体的繁琐工作交给ParallaxViewpager内部去完成
    }

    @OnClick({R.id.btn_begin, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_begin:
                yahooView.startLoading();
                break;
            case R.id.btn_stop:
                yahooView.stopLoading();
                break;
        }
    }
}
