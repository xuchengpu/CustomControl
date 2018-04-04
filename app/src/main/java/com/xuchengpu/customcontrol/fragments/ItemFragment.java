package com.xuchengpu.customcontrol.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xuchengpu.customcontrol.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends BaseFragment {


    private TextView tv;

    @Override
    public View initView() {
        tv = new TextView(mContext);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        return tv;
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        tv.setText(title);

    }
}

