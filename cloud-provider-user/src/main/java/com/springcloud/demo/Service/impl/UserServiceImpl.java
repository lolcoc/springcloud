package com.springcloud.demo.Service.impl;

import com.springcloud.demo.Dao.UserDao;
import com.springcloud.demo.Entity.User;
import com.springcloud.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public void save(User user) {
        userDao.save(user);
    }
}
