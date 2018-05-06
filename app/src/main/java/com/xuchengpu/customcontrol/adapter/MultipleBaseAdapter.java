package com.xuchengpu.customcontrol.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 许成谱 on 2018/5/5 14:16.
 * qq:1550540124
 * for:热爱生活每一天！
 * 仿58多条目筛选baseAdapter接口
 */

public abstract class MultipleBaseAdapter {

    public  abstract int  getCount();//获取菜数目

    public abstract View getTabView(int position, ViewGroup parent);//获取tab导航栏view

    public  abstract  View getMenuView(int position, ViewGroup parent);//获取菜单内容栏view

    public abstract void updateSelectedView(View selectedView,int selectedPosition);//更新选中的tab字体大小颜色等

    public abstract void updatePreView(View preView,int prePosition);//更新取消选中状态的tab字体大小颜色等

}
