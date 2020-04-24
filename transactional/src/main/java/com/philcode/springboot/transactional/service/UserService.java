package com.philcode.springboot.transactional.service;

public interface UserService {

    /**
     * 增加余额
     *
     * @param accountId 账号ID
     * @param amount 金额
     * @return true 余额操作成功，false 操作失败
     */
    boolean addBalance(long accountId, int amount);

    /**
     * 减少余额
     *
     * @param accountId 账号ID
     * @param amount 金额
     * @return true 余额操作成功，false 操作失败
     */
    boolean subtrackBalance(long accountId, int amount);

    /**
     * 账号间，资产转移
     *
     * @param fromAccountId 扣余额的账号
     * @param toAccountId 增余额的账号
     * @param amount 金额
     * @return true 余额操作成功，false 操作失败
     */
    boolean transferBalance(long fromAccountId, long toAccountId, int amount);
}
