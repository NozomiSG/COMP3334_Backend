package com.mybatisplus_comp3334.controller;


import com.mybatisplus_comp3334.entity.User;
import com.mybatisplus_comp3334.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/register")
    public String insertUserInfo(String name, String password) {
        User user = new User();
        user.setUserName(name);
        user.setUserPassword(password);
        return userMapper.insert(user)>=0?"Success" : "Failed";
    }
}
