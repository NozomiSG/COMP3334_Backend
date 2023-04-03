package com.mybatisplus_comp3334.service;

import com.mybatisplus_comp3334.entity.User;

public interface UserService {
    String insertUserInfo(User user);
    String updateUserInfo(User user);
    String deleteUserInfo(User user);
    User selectUserInfoById(Long id);
    User selectUserByEmail(String email);

}
