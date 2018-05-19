package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.YahooLoadingView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class YahooLoadingActivity extends AppCompatActivity {

    @InjectView(R.id.yahoo_view)
    YahooLoadingView yahooView;
    @InjectView(R.id.btn_begin)
    Button btnBegin;
    @InjectView(R.id.btn_stop)
    Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yahoo_loading);
        ButterKnife.inject(this);

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
