package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.LovePraiseView;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class LikeActivity extends AppCompatActivity {

    @BindView(R.id.tv_praise)
    TextView tvPraise;
    @BindView(R.id.rl_praise)
    LovePraiseView rlPraise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_praise)
    public void onViewClicked() {
        rlPraise.parise();
    }
}
