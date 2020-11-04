package com.springcloud.demo.service.impl;


import com.springcloud.demo.service.UserService;
import com.springcloud.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void save(com.springcloud.demo.entity.User user) {
        userDao.save(user);
    }
}
