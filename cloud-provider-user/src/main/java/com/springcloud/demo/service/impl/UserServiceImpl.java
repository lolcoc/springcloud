package com.springcloud.demo.service.impl;


import com.springcloud.demo.entity.User;
import com.springcloud.demo.service.UserService;
import com.springcloud.demo.dao.UserDao;
import com.springcloud.demo.util.RedisStringUtil;
import com.springcloud.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public List<User> findAll() {
        List<User> userList =null;
        boolean flag = RedisUtil.hasKey("all");
        if (flag){
            System.out.println("缓存");
            userList = (List<User>)RedisStringUtil.get("all");
        }else {
             userList = userDao.findAll();
            RedisStringUtil.set("all", userList);
        }
        RedisUtil.delete("all");
        return userList;
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
