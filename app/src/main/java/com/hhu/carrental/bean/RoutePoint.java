package com.hhu.carrental.bean;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.id;

/**
 * Created by demeiyan on 2017/5/19 00:05.
 */

public class RoutePoint implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(routeLat);
        dest.writeDouble(routeLng);
    }

    public static final Parcelable.Creator<RoutePoint> CREATOR = new Creator<RoutePoint>() {
        @Override
        public RoutePoint createFromParcel(Parcel source) {
            RoutePoint routePoint = new RoutePoint();
            routePoint.routeLat = source.readDouble();
            routePoint.routeLng = source.readDouble();
            return routePoint;
        }

        @Override
        public RoutePoint[] newArray(int size) {
// TODO Auto-generated method stub
            return new RoutePoint[size];
        }
    };
}
