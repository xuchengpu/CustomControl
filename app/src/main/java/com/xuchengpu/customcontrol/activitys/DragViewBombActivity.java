package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.DragBeiSaiErAndBombView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DragViewBombActivity extends AppCompatActivity {

    @InjectView(R.id.tv_bomb)
    TextView tvBomb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view_bomb);
        ButterKnife.inject(this);
        DragBeiSaiErAndBombView.attach(tvBomb,this);

    }
}
