package com.mybatisplus_comp3334.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.mapper.UserMapper;
import com.mybatisplus_comp3334.service.concept.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String insertUserInfo(User user) {
        log.info("Insert user info");
        if (userMapper.insert(user)>=0) {
            log.info("Success to insert user info");
            return "Success";
        } else {
            log.info("Fail to insert user info");
            return "Fail";
        }
    }

    @Override
    public String updateUserInfo(User user) {
        log.info("Update user info");
        if (userMapper.updateById(user)>=0) {
            log.info("Success to update user info");
            return "Success";
        } else {
            log.info("Fail to update user info");
            return "Fail";
        }
    }

    @Override
    public String deleteUserInfo(User user) {
        log.info("Delete user info");
        if (userMapper.deleteById(user.getUserId())>=0) {
            log.info("Success to delete user info");
            return "Success";
        } else {
            log.info("Fail to delete user info");
            return "Fail";
        }
    }

    @Override
    public User selectUserInfoById(Long id) {
        log.info("Select user info by id");
        return userMapper.selectById(id);
    }

    @Override
    public User selectUserByEmail(String email) {
        log.info("Select user info by email");
        return userMapper.selectOne(new QueryWrapper<User>().eq("user_email", email));
    }
}
