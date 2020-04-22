package com.philcode.springboot.mybatis.mapper;

import com.philcode.springboot.mybatis.model.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProfileMapper {

    int insert(UserProfile userProfile);
    int updateIgnoreNullById(@Param("userProfile") UserProfile userProfile);
    UserProfile findById(@Param("id") long id);
    List<UserProfile> findAll();
}
