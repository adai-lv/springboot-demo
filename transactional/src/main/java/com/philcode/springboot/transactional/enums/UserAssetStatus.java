package com.philcode.springboot.transactional.enums;

public enum UserAssetStatus {
    NORMAL(0, "正常"),
    FREEZE(1, "冻结");

    int code;

    String desc;

    UserAssetStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
