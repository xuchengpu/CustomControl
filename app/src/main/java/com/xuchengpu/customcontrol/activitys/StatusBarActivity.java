package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.utils.StatusBarUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StatusBarActivity extends AppCompatActivity {

    @InjectView(R.id.btn_change)
    Button btnChange;
    @InjectView(R.id.btn_immersion)
    Button btnImmersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);
        ButterKnife.inject(this);
//        StatusBarUtils.setStatusBarColor(this, Color.GREEN);// 以后使用时注意：此处有bug,只能在oncreate方法里面调用，在onclick里面调用会出问题
        StatusBarUtils.setStatusBarTranslucent(this);//以后使用时注意：此处有bug,只能在oncreate方法里面调用，在onclick里面调用会出问题
    }

    @OnClick({R.id.btn_change, R.id.btn_immersion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change:
//                StatusBarUtils.setStatusBarColor(this, Color.GREEN);//不能在此处调用
                break;
            case R.id.btn_immersion:
//                StatusBarUtils.setStatusBarTranslucent(this);//不能在此处调用
                break;
        }
    }
}
