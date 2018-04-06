package com.xuchengpu.customcontrol.activitys;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 许成谱 on 2018/4/6 12:23.
 * qq:1550540124
 * 热爱生活每一天！
 */

public abstract class FlowlayoutAdapter {
    public abstract int getCount();

    public abstract View getView(int position, ViewGroup parent);
}
