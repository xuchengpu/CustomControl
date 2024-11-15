package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.TouchView;
import com.xuchengpu.customcontrol.wiget.TouchViewGroup;

import butterknife.ButterKnife;
import butterknife.BindView;

public class TouchActivity extends AppCompatActivity {

    @BindView(R.id.touchview)
    TouchView touchview;
    @BindView(R.id.touchview_in_viewgroup)
    TouchView tvInVG;
    @BindView(R.id.touchviewgroup)
    TouchViewGroup tVG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        ButterKnife.bind(this);
        touchview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("TAG", "View.setOnTouchListener-->" + event.getAction());
                return false;
            }
        });
        touchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "View.setOnClickListener");
            }
        });
        tvInVG.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("TAG", "View.setOnTouchListener-->" + event.getAction());
                return false;
            }
        });
        tvInVG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "View.setOnClickListener");
            }
        });

    }
}
