package com.xuchengpu.customcontrol.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_textview)
    Button btnTextview;
    @BindView(R.id.btn_step)
    Button btnStep;
    @BindView(R.id.btn_indicator)
    Button btnIndicator;
    @BindView(R.id.btn_imitation58)
    Button btnImitation58;
    @BindView(R.id.btn_rate)
    Button btnRate;
    @BindView(R.id.btn_index)
    Button btnIndex;
    @BindView(R.id.btn_sudoku)
    Button btnSudoku;
    @BindView(R.id.btn_flowlayout)
    Button btnFlowlayout;
    @BindView(R.id.btn_touch)
    Button btnTouch;
    @BindView(R.id.btn_kugou)
    Button btnKugou;
    @BindView(R.id.btn_draghelper)
    Button btnDraghelper;
    @BindView(R.id.btn_status)
    Button btnStatus;
    @BindView(R.id.btn_behavior)
    Button btnBehavior;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;
    @BindView(R.id.btn_slide)
    Button btnSlide;
    @BindView(R.id.btn_multiple)
    Button btnMultiple;
    @BindView(R.id.btn_circle_loading)
    Button btnCircleLoading;
    @BindView(R.id.btn_table)
    Button btnTable;
    @BindView(R.id.btn_beisaier)
    Button btnBeisaier;
    @BindView(R.id.btn_bomb)
    Button btnBomb;
    @BindView(R.id.btn_like)
    Button btnLike;
    @BindView(R.id.btn_parallax_viewpager)
    Button btnParallaxViewpager;
    @BindView(R.id.btn_yahoo_loading)
    Button btnYahooLoading;
    @BindView(R.id.btn_parallay)
    Button btnParallay;
    @BindView(R.id.btn_matrix)
    Button btnMatrix;
    @BindView(R.id.btn_constraint)
    Button btnConstraint;
    @BindView(R.id.btn_circle)
    Button btnCircle;
    @BindView(R.id.btn_nest_scroll)
    Button btnNestScroll;
    @BindView(R.id.btn_nofication)
    Button btnNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_circle,R.id.btn_constraint,R.id.btn_matrix,R.id.btn_parallay,R.id.btn_yahoo_loading,R.id.btn_parallax_viewpager,
            R.id.btn_like, R.id.btn_bomb, R.id.btn_beisaier, R.id.btn_table, R.id.btn_circle_loading, R.id.btn_multiple, R.id.btn_slide,
            R.id.btn_refresh, R.id.btn_behavior, R.id.btn_status, R.id.btn_draghelper, R.id.btn_kugou, R.id.btn_touch, R.id.btn_flowlayout,
            R.id.btn_sudoku, R.id.btn_index, R.id.btn_textview, R.id.btn_step, R.id.btn_indicator, R.id.btn_imitation58, R.id.btn_rate,
    R.id.btn_nest_scroll,R.id.btn_nofication})
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
            case R.id.btn_circle_loading:
                intent = new Intent(this, CirlceLoadingActivity.class);
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
            case R.id.btn_draghelper:
                intent = new Intent(this, ViewDragHelperActivity.class);
                break;
            case R.id.btn_status:
                intent = new Intent(this, StatusBarActivity.class);
                break;
            case R.id.btn_behavior:
                intent = new Intent(this, BehaviorActivity.class);
                break;
            case R.id.btn_refresh:
                intent = new Intent(this, RefreshActivity.class);
                break;
            case R.id.btn_slide:
                intent = new Intent(this, SlidingActivity.class);
                break;
            case R.id.btn_multiple:
                intent = new Intent(this, MultipleMenuSelectorActivity.class);
                break;
            case R.id.btn_table:
                intent = new Intent(this, TableActivity.class);
                break;
            case R.id.btn_beisaier:
                intent = new Intent(this, BaiSaiErActivity.class);
                break;
            case R.id.btn_bomb:
                intent = new Intent(this, DragViewBombActivity.class);
                break;
            case R.id.btn_like:
                intent = new Intent(this, LikeActivity.class);
                break;
            case R.id.btn_parallax_viewpager:
                intent = new Intent(this, ParallaxActivity.class);
                break;
            case R.id.btn_yahoo_loading:
                intent = new Intent(this, YahooLoadingActivity.class);
                break;
            case R.id.btn_parallay:
                intent = new Intent(this, ParallayActivity.class);
                break;
            case R.id.btn_matrix:
                intent = new Intent(this, MatrixActivity.class);
                break;
            case R.id.btn_constraint:
                intent = new Intent(this, ConstraintActivity.class);
                break;
            case R.id.btn_circle:
                intent = new Intent(this, CircleShadowActivity.class);
                break;
            case R.id.btn_nest_scroll:
                intent = new Intent(this, NestScrollActivity.class);
                break;
            case R.id.btn_nofication:
                createNotification();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void createNotification() {

    }
}
