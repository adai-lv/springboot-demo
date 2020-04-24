package com.philcode.springboot.transactional.service.impl;

import com.philcode.springboot.transactional.entity.UserAsset;
import com.philcode.springboot.transactional.enums.UserAssetStatus;
import com.philcode.springboot.transactional.repository.UserAssetRepository;
import com.philcode.springboot.transactional.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserAssetRepository userAssetRepository;

    public UserServiceImpl(UserAssetRepository userAssetRepository) {
        this.userAssetRepository = userAssetRepository;
    }

    /**
     * 增加余额
     *
     * 调用 Repository 经过 @Modifying 注解过的方法（insert,update,delete）
     * 需要加事务管理注解 @Transactional
     *
     * @param accountId 账号ID
     * @param amount 金额
     * @return true 余额操作成功，false 操作失败
     */
    @Override
    @Transactional
    public boolean addBalance(long accountId, int amount) {
        if (accountId <= 0 || amount <= 0) {
            return false;
        }

        int result = userAssetRepository.increase(accountId, amount, UserAssetStatus.NORMAL.getCode());

        return result > 0;
    }

    /**
     * 减少余额
     *
     * 调用 Repository 经过 @Modifying 注解过的方法（insert,update,delete）
     * 需要加事务管理注解 @Transactional
     *
     * @param accountId 账号ID
     * @param amount 金额
     * @return true 余额操作成功，false 操作失败
     */
    @Override
    @Transactional
    public boolean subtrackBalance(long accountId, int amount) {

        if (accountId <= 0 || amount <= 0) {
            return false;
        }

        int result = userAssetRepository.decrease(accountId, amount, UserAssetStatus.NORMAL.getCode());

        return result > 0;
    }

    /**
     * 账号间，资产转移
     *
     * @param fromAccountId 扣余额的账号
     * @param toAccountId   增余额的账号
     * @param amount        金额
     * @return true 余额操作成功，false 操作失败
     */
    @Override
    @Transactional
    public boolean transferBalance(long fromAccountId, long toAccountId, int amount) {

        if (fromAccountId < 0 || toAccountId < 0) {
            throw new RuntimeException("转账账号错误，请确认");
        }

        if (fromAccountId == toAccountId) {
            throw new RuntimeException("不支持同一个账号内转账");
        }

        if (amount <= 0) {
            throw new RuntimeException("转账的金额要大于0");
        }

        Optional<UserAsset> fromUser = userAssetRepository.findByAccountId(fromAccountId);
        Optional<UserAsset> toUser = userAssetRepository.findByAccountId(toAccountId);

        if (!fromUser.isPresent()) {
            throw new RuntimeException("扣余额的账号不存在");
        }

        if (!toUser.isPresent()) {
            throw new RuntimeException("接收的账号不存在");
        }

        // 扣余额
        UserAsset fromUserAsset = fromUser.get();
        int decreaseAmount = fromUserAsset.getBalance() - amount;

        if (decreaseAmount < 0) {
            throw new RuntimeException("fromUser资产不够");
        }

        fromUserAsset.setBalance(decreaseAmount);
        userAssetRepository.save(fromUserAsset);


        // 增加余额
        UserAsset toUserAsset = toUser.get();
        int increaseAmount = toUserAsset.getBalance() + amount;
        toUserAsset.setBalance(increaseAmount);

        //假设转账的时候假如出现异常，业务类或业务方法中没有使用@Transactional控制事务，则会出现钱转出了，收钱人没有收到的情况
        //打开这个异常
        //int zero = 1/0;
        userAssetRepository.save(toUserAsset);

        return true;
    }

}
