package com.philcode.springboot.mybatis.mapper;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.philcode.springboot.mybatis.model.entity.Account;
import com.philcode.springboot.mybatis.model.entity.AccountExtend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountMapperTest {

    @Resource
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
        PageHelper.startPage(1, 2);

        List<Account> accounts = accountMapper.findAll();

        PageInfo<Account> page = new PageInfo<>(accounts);

        System.out.println(JSON.toJSONString(page));
    }

    @Test
    public void findWithStudentsById() {

        AccountExtend result = accountMapper.findWithUserProfileById(1L);

        System.out.println(JSON.toJSONString(result));
    }

    /**
     * 有三点需要说明一下：
     * 1. 为什么开启事务？由于使用了数据库连接池，默认每次查询完之后自动commite，这就导致两次查询使用的不是同一个sqlSessioin，根据一级缓存的原理，它将永远不会生效。
     *    当开启了事务，两次查询都在同一个sqlSession中，从而让第二次查询命中了一级缓存。可以关闭事务验证此结论。
     * 2. 一级缓存的作用域有两种：session（默认）和 statment，可通过设置 mybatis.configuration.local-cache-scope: SESSION | STATEMENT 的值来切换。
     *    二者的区别在于 session 会将缓存作用于同一个 sqlSesson，而 statment 仅针对一次查询，所以 local-cache-scope: STATEMENT 可以理解为关闭一级缓存。
     * 3. 如果作用域于同一sqlSession内，由于无法感知到其他sqlSession的增删改，必然导致查询出脏数据；
     *    如果应用是分布式部署，由于一级缓存存储在本地，必然导致查询出脏数据；
     *    所以，生产环境的应用不建议开启，建议将一级缓存设置为statment级别。
     */
    @Test
    @Transactional(rollbackFor = Throwable.class)
    public void findByIdOfOneCache() {
        System.out.println(accountMapper.findById(2L));
        System.out.println(accountMapper.findById(2L));
        System.out.println(accountMapper.findById(2L));
    }

    /**
     * 默认情况下，mybatis打开了二级缓存，但它并未生效，因为二级缓存的作用域是namespace，所以还需要在对应的 *Mapper.xml 文件中配置一下才能使二级缓存生效
     * <cache />
     *
     * 如果应用是分布式部署，由于二级缓存存储在本地，必然导致查询出脏数据，所以分布式部署的应用启用第三方缓存服务（如：redis）。
     */
    @Test
    public void findByIdOfSecondCache() {
        System.out.println(accountMapper.findById(3L));
        System.out.println(accountMapper.findById(3L));
        System.out.println(accountMapper.findById(3L));
    }
}