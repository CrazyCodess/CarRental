package com.hhu.carrental.bean;

import cn.bmob.im.bean.BmobChatUser;

/**
 * 用户信息
 * Created by demeiyan on 2017/5/4 10:41.
 */

public class User extends BmobChatUser {

    private String school;
    private Boolean sex;//性别true为男
    private String date;//生日
    private Integer incredits;//积分

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getIncredits() {
        return incredits;
    }

    public void setIncredits(Integer incredits) {
        this.incredits = incredits;
    }
}
