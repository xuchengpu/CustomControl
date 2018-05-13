package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.LovePraiseView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LikeActivity extends AppCompatActivity {

    @InjectView(R.id.tv_praise)
    TextView tvPraise;
    @InjectView(R.id.rl_praise)
    LovePraiseView rlPraise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.tv_praise)
    public void onViewClicked() {
        rlPraise.parise();
    }
}
