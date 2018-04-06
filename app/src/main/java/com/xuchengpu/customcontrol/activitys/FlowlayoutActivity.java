package com.xuchengpu.customcontrol.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.wiget.Flowlayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlowlayoutActivity extends AppCompatActivity {
    @InjectView(R.id.flowlayout)
    Flowlayout flowlayout;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initView() {
        flowlayout.setAdapter(new FlowlayoutAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public View getView(final int position, ViewGroup parent) {
                final android.widget.TextView textView = (android.widget.TextView) LayoutInflater.from(FlowlayoutActivity.this).inflate(R.layout.tag_view, parent, false);
                textView.setText(datas.get(position));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FlowlayoutActivity.this, "position=="+position, Toast.LENGTH_SHORT).show();
                        textView.setBackgroundColor(Color.BLUE);
                    }
                });

                return textView;
            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        datas.add("aaaa");
        datas.add("aaaaaaaaaa");
        datas.add("aaaaa");
        datas.add("aa");
        datas.add("aaaaaaaaaaaaa");
        datas.add("aaaaaaa");
        datas.add("aaaaaaaaaaa");
        datas.add("a");
        datas.add("aaaa");
        datas.add("aaaaaaaaaa");
        datas.add("aaaaa");
        datas.add("aa");
        datas.add("aaaaaaaaaaaaa");
        datas.add("aaaaaaa");
        datas.add("aaaaaaaaaaa");
        datas.add("a");
    }
}
