package com.xuchengpu.customcontrol.utils;

/**
 * Created by 许成谱 on 2018/5/17 17:44.
 * qq:1550540124
 * 热爱生活每一天！
 * 对tag的一种封装，可多携带几个参数信息
 */

public class ParallaxTag {
    public float translationXIn;
    public float translationXOut;
    public float translationYIn;
    public float translationYOut;

    @Override
    public String toString() {
        return "ParallaxTag{" +
                "translationXIn=" + translationXIn +
                ", translationXOut=" + translationXOut +
                ", translationYIn=" + translationYIn +
                ", translationYOut=" + translationYOut +
                '}';
    }
}
