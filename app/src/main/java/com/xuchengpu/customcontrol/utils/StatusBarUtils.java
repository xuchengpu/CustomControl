package com.xuchengpu.customcontrol.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by 许成谱 on 2018/4/11 15:59.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class StatusBarUtils {

    /**改变状态栏颜色
     * @param activity
     */
    public static void setStatusBarColor(Activity activity,@ColorInt int color) {
        //4.4以下系统没法调

        //5.0以上 用系统提供好的方法
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
        //4.4-5.0 先弄成全屏，然后在头部添加一个同样大小的布局
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//让状态栏变成透明，此时电量等图标显示皆保留

            //添加一个同样大小的布局到decorView
            View view=new View(activity);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusHeight(activity));
            view.setLayoutParams(params);
            view.setBackgroundColor(color);

            decorView.addView(view);//实际上decorview是一个framlayout


            // 获取activity中setContentView布局的根布局
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
//            contentView.setPadding(0,getStatusHeight(activity),0,0);
             View activityView = contentView.getChildAt(0);
             activityView.setFitsSystemWindows(true);
            // activityView.setPadding(0,getStatusBarHeight(activity),0,0);

        }
    }
    public static void setStatusBarTranslucent(Activity activity){
        //5.0以上 用系统提供好的方法
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        //4.4-5.0 先弄成全屏，然后在头部添加一个同样大小的布局
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//让状态栏变成透明，此时电量等图标显示皆保留

        }


    }

    /**获取状态栏高度
     * @param activity
     * @return
     */
    private static int getStatusHeight(Activity activity) {
        // 插件式换肤：怎么获取资源的，先获取资源id，根据id获取资源
        Resources resources = activity.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelOffset(identifier);
        return height;
    }
}
