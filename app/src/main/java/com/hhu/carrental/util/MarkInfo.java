package com.hhu.carrental.util;

import java.io.Serializable;

/**
 * Created by demeiyan on 2017/4/8 21:25.
 */

public class MarkInfo implements Serializable {
    private double latitude;
    private double longtitude;
    private String name;
    private int imageid;
    private String desc;

    public MarkInfo() {
    }

    public MarkInfo(double latitude, double longtitude, String name, int imageid, String desc) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
        this.imageid = imageid;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "MarkInfo{" +
                "latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", name='" + name + '\'' +
                ", imageid=" + imageid +
                ", desc='" + desc + '\'' +
                '}';
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
