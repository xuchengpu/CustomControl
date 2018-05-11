package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

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
        DragBeiSaiErAndBombView.attach(tvBomb,this,new BombListener(){

            @Override
            public void dismiss() {
                //此处可用于操作通知后台等
                Toast.makeText(DragViewBombActivity.this, "消失了", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 拖动超过一定距离后view消失通知
     */
    public interface BombListener{
        void dismiss();
    }
}
