package com.springcloud.demo.service.impl;


import com.springcloud.demo.entity.User;
import com.springcloud.demo.service.UserService;
import com.springcloud.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void findAll() {
        System.out.println("-------------------------->>>>>>>>>>");
    }

    @Override
    public User selectUserByName(String userName) {
        return userDao.selectUserByName(userName);
    }

    @Override
    public User selectUserByIDCard(String idCard) {
        return userDao.selectUserByIDCard(idCard);
    }

    @Override
    public User selectUserByphoneNumber(String phoneNumber) {
        return userDao.selectUserByphoneNumber(phoneNumber);
    }
}
