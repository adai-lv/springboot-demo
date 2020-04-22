package com.philcode.springboot.mybatis.mapper;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.philcode.springboot.mybatis.model.dao.Account;
import com.philcode.springboot.mybatis.model.dao.AccountExtend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void insert() {

        for (int i = 1; i <= 15; i++) {

            Account account = new Account();
            account.setUsername("banji" + i);
            account.setPassword(UUID.randomUUID().toString());
            account.setNickname("bigo" + i);
            account.setIcon("");
            account.setStatus(0);
            account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            accountMapper.insert(account);
        }
    }

    @Test
    public void updateIgnoreNullById() {

        Account account = new Account();
        account.setId(1L);
        account.setPassword(UUID.randomUUID().toString());
        account.setNickname("bigo00000");
        account.setIcon("icon.jpg");
        account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        accountMapper.updateIgnoreNullById(account);
    }

    @Test
    public void deleteById() {
        accountMapper.deleteById(2L);
    }

    @Test
    public void findById() {
        Account account = accountMapper.findById(1L);

        System.out.println(account);
    }

    @Test
    public void findAll() {

        //获取第 1 页，10 条内容，默认查询总数 count
        PageHelper.startPage(1, 10);

        List<Account> accounts = accountMapper.findAll();

        PageInfo<List<Account>> page = new PageInfo(accounts);

        System.out.println(JSON.toJSONString(page));
    }

    @Test
    public void findWithStudentsById() {

        AccountExtend result = accountMapper.findWithUserProfileById(1L);

        System.out.println(JSON.toJSONString(result));
    }
}