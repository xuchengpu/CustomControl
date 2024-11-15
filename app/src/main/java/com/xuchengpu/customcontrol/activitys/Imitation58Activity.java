package com.xuchengpu.customcontrol.activitys;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.LoadView58;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class Imitation58Activity extends AppCompatActivity {

    @BindView(R.id.ita_58)
    LoadView58 ita58;
    @BindView(R.id.btn_setvisiable)
    Button btnSetvisiable;
    @BindView(R.id.btn_setinvisiable)
    Button btnSetinvisiable;
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imitation58);
        ButterKnife.bind(this);


//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                while (true) {
//                    try {
//                        Thread.sleep(800);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ita58.change();
//                        }
//                    });
//
//                }
//            }
//        }.start();
    }

    @OnClick({R.id.btn_setvisiable, R.id.btn_setinvisiable})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setvisiable:
                ita58.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_setinvisiable:
                ita58.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ita58.setVisibility(View.GONE);
    }
}
