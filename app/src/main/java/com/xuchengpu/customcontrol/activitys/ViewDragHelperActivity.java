package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.TouchView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewDragHelperActivity extends AppCompatActivity {

    @InjectView(R.id.tv1)
    TouchView  tv1;
    @InjectView(R.id.tv2)
    TouchView  tv2;
    @InjectView(R.id.tv3)
    TouchView  tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drag_helper);
        ButterKnife.inject(this);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewDragHelperActivity.this, "tv3", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
