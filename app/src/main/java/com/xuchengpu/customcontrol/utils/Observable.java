package com.xuchengpu.customcontrol.utils;

/**
 * Created by 许成谱 on 2018/5/7 12:32.
 * qq:1550540124
 * 热爱生活每一天！
 * 观察者模式之抽象主题，即被观察者
 */

public abstract class Observable {
    /**
     * 注册（订阅）
     * @param observer
     */
    public abstract void register(Observer observer);

    /**
     * 解注册（退订）
     * @param observer
     */
    public abstract void unRegister(Observer observer);

    /**
     * 发出通知（推送）
     */
    public abstract void sendNotification();
}
