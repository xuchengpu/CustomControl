package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.CircleShadowImageView;

public class CircleShadowActivity extends AppCompatActivity {

    private CircleShadowImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_shadow);
        imageView = findViewById(R.id.iv_shadow);
        imageView.setImageResource(R.drawable.apple_pic);
    }
}
