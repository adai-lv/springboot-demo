package com.philcode.springboot.mybatis.mapper;

import com.philcode.springboot.mybatis.model.entity.Account;
import com.philcode.springboot.mybatis.model.entity.AccountExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {
    int insert(Account account);
    int updateIgnoreNullById(@Param("account") Account account);
    int deleteById(@Param("id") long id);
    Account findById(@Param("id") long id);
    List<Account> findAll();

    /**
     * 根据id查询账号信息，并带出其用户档案信息
     *
     * @param id 账号id
     * @return 账号信息+用户档案信息
     */
    AccountExtend findWithUserProfileById(@Param("id") long id);
}
