package com.philcode.springboot.mybatis.mapper;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.philcode.springboot.mybatis.model.entity.UserProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProfileMapperTest {

    @Resource
    private UserProfileMapper userProfileMapper;

    @Test
    public void insert() {

        for (int i = 1; i <= 15; i++) {

            UserProfile userProfile = new UserProfile();
            userProfile.setRealname("lvmaohai" + i);
            userProfile.setBirtyday(new Date(System.currentTimeMillis()));
            userProfile.setAccountId((long)i);
            userProfile.setGender("M");
            userProfile.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userProfile.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            userProfileMapper.insert(userProfile);
        }
    }

    @Test
    public void findById() {

        UserProfile userProfile = userProfileMapper.findById(1L);

        System.out.println(JSON.toJSONString(userProfile));
    }

    @Test
    public void findAll() {

        //获取第 1 页，10 条内容，默认查询总数 count
        PageHelper.startPage(1, 5);

        List<UserProfile> userProfiles = userProfileMapper.findAll();

        PageInfo<UserProfile> page = new PageInfo<>(userProfiles);

        System.out.println(JSON.toJSONString(page));
    }
}