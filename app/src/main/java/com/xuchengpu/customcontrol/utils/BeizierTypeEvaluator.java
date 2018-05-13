package com.xuchengpu.customcontrol.utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by 许成谱 on 2018/5/13 21:13.
 * qq:1550540124
 * for:热爱生活每一天！
 * 贝塞尔曲线的估值器
 *
 */

public class BeizierTypeEvaluator implements TypeEvaluator<PointF> {

    private final PointF point1;//控制点1
    private final PointF point2;//控制点2

    public BeizierTypeEvaluator(PointF point1, PointF point2) {
        this.point1=point1;
        this.point2=point2;
    }

    /**
     *
     * @param t 变量 【0,1】
     * @param point0 起始点
     * @param point3 终点
     * @return
     */
    @Override
    public PointF evaluate(float t, PointF point0, PointF point3) {
        //三阶贝瑟尔曲线公式
        PointF pointF =new PointF();
        pointF.x=point0.x*(1-t)*(1-t)*(1-t)+3*point1.x*t*(1-t)*(1-t)+3*point2.x*t*t*(1-t)+point3.x*t*t*t;
        pointF.y=point0.y*(1-t)*(1-t)*(1-t)+3*point1.y*t*(1-t)*(1-t)+3*point2.y*t*t*(1-t)+point3.y*t*t*t;
        return pointF;
    }
}
