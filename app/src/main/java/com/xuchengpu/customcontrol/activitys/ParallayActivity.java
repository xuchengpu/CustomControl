package com.xuchengpu.customcontrol.activitys;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xuchengpu.customcontrol.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ParallayActivity extends AppCompatActivity {

    @InjectView(R.id.imageview)
    ImageView imageview;
    @InjectView(R.id.ll_indicator)
    LinearLayout llIndicator;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallay);
        ButterKnife.inject(this);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                imageview.setTranslationY((float) (scrollY*0.5));
                imageview.setAlpha(1-(float)scrollY/imageview.getMeasuredHeight());
            }
        });
    }
}
