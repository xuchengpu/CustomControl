package com.xuchengpu.customcontrol.bean;

/**
 * Created by 许成谱 on 2018/1/30 11:24.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class Fruit {
    private String name;
    private  int imgId;

    public Fruit(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "name='" + name + '\'' +
                ", imgId=" + imgId +
                '}';
    }
}
