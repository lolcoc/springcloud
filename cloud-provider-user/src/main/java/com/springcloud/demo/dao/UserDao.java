package com.springcloud.demo.dao;

import com.springcloud.demo.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:23
 * @Description: 用户数据库操作类
 */
@Repository
public class UserDao extends BaseDao {

    /**
     * 保存用户
     * @param user
     * @return
     */
    public Integer save(User user) {
        String sql = "insert into sys_user(id, userName, passWord) values(?,?,?)";
        Integer count = jdbcTemplate.update(sql, user.getId(), user.getUserName(), user.getPassWord());
        return count;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    public Integer delete(String id) {
        String sql = "delete from sys_user where id=?";
        Integer count = jdbcTemplate.update(sql, id);
        return count;
    }

    /**
     * 查询全部用户
     *
     * @return
     */
    public List<User> findAll() {
        String sql = "select * from sys_user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));
    }
}
