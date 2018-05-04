package com.xuchengpu.customcontrol.activitys;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.LoadView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Imitation58Activity extends AppCompatActivity {

    @InjectView(R.id.ita_58)
    LoadView  ita58;
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imitation58);
        ButterKnife.inject(this);


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
}
