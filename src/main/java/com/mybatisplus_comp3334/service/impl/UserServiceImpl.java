package com.mybatisplus_comp3334.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.mapper.UserMapper;
import com.mybatisplus_comp3334.service.MailService;
import com.mybatisplus_comp3334.service.UserService;
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

    @Autowired
    private MailService mailService;

    @Override
    public String insertUserInfo(User user) {
        if (userMapper.insert(user)<0) {
            log.info("Fail to insert user");
            return "Fail";
        }
        String code = user.getActivation_code();
        log.info("Activation code: " + code);
        String subject = "Activation email from P2PMall";
        String content = "Please click the link below to activate your account: http://localhost:8088/activate?code=" + code;
        log.info("Send a mail to " + user.getUserEmail());
        mailService.sentSimpleMail(user.getUserEmail(), subject, content);
        return "Success";
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
