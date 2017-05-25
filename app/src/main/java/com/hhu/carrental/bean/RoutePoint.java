package com.hhu.carrental.bean;

import java.io.Serializable;

/**
 * Created by demeiyan on 2017/5/19 00:05.
 */

public class RoutePoint implements Serializable {

    double routeLat,routeLng;

    public double getRouteLat() {
        return routeLat;
    }

    public void setRouteLat(double routeLat) {
        this.routeLat = routeLat;
    }

    public double getRouteLng() {
        return routeLng;
    }

    public void setRouteLng(double routeLng) {
        this.routeLng = routeLng;
    }
}
