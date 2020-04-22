package com.philcode.springboot.mybatis.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class UserProfile implements Serializable {

    private static final long serialVersionUID = 5577779755464051585L;

    /**
     * 代理键
     */
    private Long id;

    /**
     * 账号ID
     */
    private Long accountId;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 生日，格式：yyyy-mm-dd
     */
    private Date birtyday;

    /**
     * 性别，M男性，F女性
     */
    private String gender;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Date getBirtyday() {
        return birtyday;
    }

    public void setBirtyday(Date birtyday) {
        this.birtyday = birtyday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", realname='" + realname + '\'' +
                ", birtyday=" + birtyday +
                ", gender='" + gender + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
