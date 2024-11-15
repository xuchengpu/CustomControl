package com.xuchengpu.customcontrol.activitys;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.CircleProgressBar;
import com.xuchengpu.customcontrol.wiget.StepView;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class StepActivity extends AppCompatActivity {


    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.reset)
    Button reset;
    @BindView(R.id.reset_2)
    Button reset2;
    @BindView(R.id.circle_progressbar)
    CircleProgressBar circleProgressbar;
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        stepView.setMaxStep(5000);
        circleProgressbar.setMaxValue(4000);
        resetAnimator();

    }


    private void resetAnimator() {

        animator = ObjectAnimator.ofFloat(0, 3500);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                stepView.setCurrentStep((int) animatedValue);
            }
        });
        animator.setInterpolator(new BounceInterpolator());
        animator.start();

    }

    @OnClick({R.id.reset, R.id.reset_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                resetAnimator();
                break;
            case R.id.reset_2:
                resetAnimator2();
                break;
        }
    }

    private void resetAnimator2() {
        animator = ObjectAnimator.ofFloat(0, 3000);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                circleProgressbar.setProgress(animatedValue);
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
}
