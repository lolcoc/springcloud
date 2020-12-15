package com.springcloud.demo.service;

import com.springcloud.demo.entity.User;

import java.util.List;

public interface UserService {

    void save(User user);

    List<User> findAll();

    User selectUserByName(String userName);

    User selectUserByIDCard(String idCard);

    User selectUserByphoneNumber(String phoneNumber);
}
