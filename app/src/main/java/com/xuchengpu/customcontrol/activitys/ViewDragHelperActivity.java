package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuchengpu.customcontrol.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class ViewDragHelperActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView listview;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drag_helper);
        ButterKnife.bind(this);

        for (int i = 0; i < 30; i++) {
            datas.add("item-->" + i);
        }
        listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                MyViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(ViewDragHelperActivity.this).inflate(R.layout.item_lv, parent, false);
                    holder=new MyViewHolder();
                    holder.tv= (TextView) convertView;
                    convertView.setTag(holder);
                }else{
                    holder= (MyViewHolder) convertView.getTag();
                }
                holder.tv.setText(datas.get(position));
                return convertView;
            }
            class MyViewHolder {
                TextView tv;
            }
        });

    }
}
