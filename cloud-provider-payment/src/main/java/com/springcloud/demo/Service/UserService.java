package com.springcloud.demo.Service;

import com.springcloud.demo.Dao.UserDao;
import com.springcloud.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:45
 * @Description: 用户业务实现类
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 保存用户
     *
     * @param user
     */
    public void save(User user) {
        Integer count = userDao.save(user);
    }
}
