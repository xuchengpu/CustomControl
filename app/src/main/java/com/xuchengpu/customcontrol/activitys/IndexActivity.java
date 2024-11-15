package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.IndexView;

import butterknife.ButterKnife;
import butterknife.BindView;

public class IndexActivity extends AppCompatActivity {

    @BindView(R.id.textview)
    TextView textview;
    @BindView(R.id.index_view)
    IndexView indexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);

        indexView.setOnPositonListener(new IndexView.OnPositonListener() {
            @Override
            public void Touch(String letter) {
                textview.setVisibility(View.VISIBLE);
                textview.setText(letter);
            }

            @Override
            public void up() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String name = Thread.currentThread().getName();
                        Log.e("TAG", "name==" + name);//运行在主线程
                        textview.setVisibility(View.GONE);
                    }
                }, 1000);
            }

        });
    }
}
