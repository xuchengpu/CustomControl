package com.xuchengpu.customcontrol.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xuchengpu.customcontrol.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.btn_textview)
    Button btnTextview;
    @InjectView(R.id.btn_step)
    Button btnStep;
    @InjectView(R.id.btn_indicator)
    Button btnIndicator;
    @InjectView(R.id.btn_imitation58)
    Button btnImitation58;
    @InjectView(R.id.btn_rate)
    Button btnRate;
    @InjectView(R.id.btn_index)
    Button btnIndex;
    @InjectView(R.id.btn_sudoku)
    Button btnSudoku;
    @InjectView(R.id.btn_flowlayout)
    Button btnFlowlayout;
    @InjectView(R.id.btn_touch)
    Button btnTouch;
    @InjectView(R.id.btn_kugou)
    Button btnKugou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.btn_kugou,R.id.btn_touch, R.id.btn_flowlayout, R.id.btn_sudoku, R.id.btn_index, R.id.btn_textview, R.id.btn_step, R.id.btn_indicator, R.id.btn_imitation58, R.id.btn_rate})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_textview:
                intent = new Intent(this, MyTextViewActivity.class);
                break;
            case R.id.btn_step:
                intent = new Intent(this, StepActivity.class);
                break;
            case R.id.btn_indicator:
                intent = new Intent(this, IndicatorActivity.class);
                break;
            case R.id.btn_imitation58:
                intent = new Intent(this, Imitation58Activity.class);
                break;
            case R.id.btn_rate:
                intent = new Intent(this, RatingViewActivity.class);
                break;
            case R.id.btn_index:
                intent = new Intent(this, IndexActivity.class);
                break;
            case R.id.btn_sudoku:
                intent = new Intent(this, SudokuActivity.class);
                break;
            case R.id.btn_flowlayout:
                intent = new Intent(this, FlowlayoutActivity.class);
                break;
            case R.id.btn_touch:
                intent = new Intent(this, TouchActivity.class);
                break;
            case R.id.btn_kugou:
                intent = new Intent(this, KuGouActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }

    }

}
