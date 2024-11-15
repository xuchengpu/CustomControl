package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.DragBeiSaiErAndBombView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DragViewBombActivity extends AppCompatActivity {

    @BindView(R.id.tv_bomb)
    TextView tvBomb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view_bomb);
        ButterKnife.bind(this);
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
