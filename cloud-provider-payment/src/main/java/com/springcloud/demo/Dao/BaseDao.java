package com.springcloud.demo.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:25
 * @Description: 数据库连接父类
 */
public class BaseDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
}
