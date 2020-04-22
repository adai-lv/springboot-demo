package com.philcode.springboot.mybatis.model.entity;

import java.io.Serializable;

public class AccountExtend extends Account implements Serializable {

    private static final long serialVersionUID = -7134894362029063218L;

    /**
     * 用户档案
     */
    private UserProfile userProfile;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return "AccountExtend{" +
                "userProfile=" + userProfile +
                '}';
    }
}
