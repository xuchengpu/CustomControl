package com.xuchengpu.customcontrol.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.adapter.MultipleBaseAdapter;
import com.xuchengpu.customcontrol.wiget.MultipleMenuSelectorView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MultipleMenuSelectorActivity extends AppCompatActivity {

    @InjectView(R.id.multiple_view)
    MultipleMenuSelectorView multipleView;
    private String[] mItems = {"类型", "品牌", "价格", "更多"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_menu_selector);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        multipleView.setAdapter(new MultipleBaseAdapter() {
            @Override
            public int getCount() {
                return mItems.length;
            }

            @Override
            public View getTabView(int position, ViewGroup parent) {
                android.widget.TextView tabView = (android.widget.TextView) LayoutInflater.from(MultipleMenuSelectorActivity.this).inflate(R.layout.multiple_tab_view, parent, false);
                tabView.setText(mItems[position]);
                return tabView;
            }

            @Override
            public View getMenuView(int position, ViewGroup parent) {
                RelativeLayout menuView = (RelativeLayout) LayoutInflater.from(MultipleMenuSelectorActivity.this).inflate(R.layout.multiple_menu_view, parent, false);
                TextView menuContent = menuView.findViewById(R.id.tv_menu_content);
                menuContent.setText(mItems[position]+"内容");
                return menuView;
            }

            @Override
            public void updateSelectedView(View selectedView, int selectedPosition) {
                TextView textView = (TextView) selectedView;
                textView.setTextColor(Color.RED);
            }

            @Override
            public void updatePreView(View preView, int prePosition) {
                TextView textView = (TextView) preView;
//                textView.setTextColor(getResources().getColor(R.color.light_black));
                textView.setTextColor(ContextCompat.getColor(MultipleMenuSelectorActivity.this,R.color.light_black));//兼容方法
            }


        });
    }
}
