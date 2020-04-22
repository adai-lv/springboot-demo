package com.philcode.springboot.mybatis.model.dao;

public class AccountExtend extends Account {

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
