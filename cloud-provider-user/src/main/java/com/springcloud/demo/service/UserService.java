package com.springcloud.demo.service;

import com.springcloud.demo.entity.User;

public interface UserService {

    void save(User user);

    void findAll();

    User selectUserByName(String userName);

    User selectUserByIDCard(String idCard);

    User selectUserByphoneNumber(String phoneNumber);
}
