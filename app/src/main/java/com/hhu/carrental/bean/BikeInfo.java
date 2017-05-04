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
    private BmobGeoPoint location;//车的位置
    private User user;
    private Boolean isUsed;//是否已被使用

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
}
