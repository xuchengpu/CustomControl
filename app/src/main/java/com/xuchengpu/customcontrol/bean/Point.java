package com.xuchengpu.customcontrol.bean;

/**
 * Created by 许成谱 on 2018/3/29 15:29.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class Point {
    private float x;
    private float y;
    private int index;

    private PointStatus status=PointStatus.normal;

    public enum PointStatus {
        pressed,normal,error
    }

    public Point(float x, float y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public PointStatus getStatus() {
        return status;
    }

    public void setStatus(PointStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                ", status=" + status +
                '}';
    }
}
