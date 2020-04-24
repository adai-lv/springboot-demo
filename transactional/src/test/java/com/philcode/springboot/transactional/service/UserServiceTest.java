package com.philcode.springboot.transactional.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void addBalance() {
        Assert.isTrue(!userService.addBalance(0, 1000), "加余额异常操作成功【账号为0】");
        Assert.isTrue(!userService.addBalance(1, 0), "加余额异常操作成功【金额为0】");
        Assert.isTrue(!userService.addBalance(1, -1000), "加余额异常操作成功【金额为负数】");
        Assert.isTrue(userService.addBalance(1, 1000), "加余额正常操作失败");
    }

    @Test
    public void subtrackBalance() {
        Assert.isTrue(!userService.subtrackBalance(0, 1000), "减余额异常操作成功【账号为0】");
        Assert.isTrue(!userService.subtrackBalance(1, 0), "减余额异常操作成功【金额为0】");
        Assert.isTrue(!userService.subtrackBalance(1, -1000), "减余额异常操作成功【金额为负数】");
        Assert.isTrue(userService.subtrackBalance(1, 3000), "减余额正常操作失败");
    }

    @Test
    public void transferBalance() {
        Assert.isTrue(userService.transferBalance(1, 2, 100), "转账操作失败");
    }
}