package com.hhu.carrental.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 单车信息
 * Created by demeiyan on 2017/5/4 15:00.
 */

public class BikeInfo extends BmobObject {

    //private String bikeNo;//单车编号
    private String unlockPass;//锁密码
    private BmobGeoPoint location;
    private User user;
    private Boolean isUsed;//是否已被使用
    private String phoneNumber;
    private String bikeType;
    private String bikeDetail;
    private String rentTime;

    public BikeInfo(String unlockPass, BmobGeoPoint location
            , User user, String phoneNumber, String bikeType, String bikeDetail, String rentTime) {
        this.unlockPass = unlockPass;
        this.location = location;
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.bikeType = bikeType;
        this.bikeDetail = bikeDetail;
        this.rentTime = rentTime;
        this.isUsed = false;
    }

    public BikeInfo() {
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    public String getBikeDetail() {
        return bikeDetail;
    }

    public void setBikeDetail(String bikeDetail) {
        this.bikeDetail = bikeDetail;
    }

    public String getRentTime() {
        return rentTime;
    }

    public void setRentTime(String rentTime) {
        this.rentTime = rentTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUnlockPass() {
        return unlockPass;
    }

    public void setUnlockPass(String unlockPass) {
        this.unlockPass = unlockPass;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    @Override
    public String toString() {
        return "BikeInfo{" +
                "unlockPass='" + unlockPass + '\'' +
                ", location=" + location +
                ", user=" + user +
                ", isUsed=" + isUsed +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bikeType='" + bikeType + '\'' +
                ", bikeDetail='" + bikeDetail + '\'' +
                ", rentTime='" + rentTime + '\'' +
                '}';
    }
}
