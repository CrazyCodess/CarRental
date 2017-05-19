package com.hhu.carrental.bean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by demeiyan on 2017/5/19 02:37.
 */

public class RouteInfo extends BmobObject {

    private String bikeId;
    private String bikingCost;
    private String bikingTime;
    //private String bikingDate;
    private User user;
    ArrayList<RoutePoint> routeList;

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikingCost() {
        return bikingCost;
    }

    public void setBikingCost(String bikingCost) {
        this.bikingCost = bikingCost;
    }

    public String getBikingTime() {
        return bikingTime;
    }

    public void setBikingTime(String bikingTime) {
        this.bikingTime = bikingTime;
    }



    public ArrayList<RoutePoint> getRouteList() {
        return routeList;
    }

    public void setRouteList(ArrayList<RoutePoint> routeList) {
        this.routeList = routeList;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
