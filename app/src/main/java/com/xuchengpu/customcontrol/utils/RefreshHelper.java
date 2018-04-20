package com.xuchengpu.customcontrol.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 许成谱 on 2018/4/19 11:08.
 * qq:1550540124
 * 热爱生活每一天！
 * for:下拉刷新，上拉加载的帮助类，可用于设置下拉刷新的动画
 */

public abstract class RefreshHelper {

    /**
     * 提供下拉刷新的view
     *
     * @param context
     * @param parent
     * @return
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     * 正在下拉时
     *
     * @param currentHeight
     * @param viewHeight
     */
    public abstract void onPull(int currentHeight, int viewHeight);

    /**
     * 正在刷新
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void stopRefreshing();
}
