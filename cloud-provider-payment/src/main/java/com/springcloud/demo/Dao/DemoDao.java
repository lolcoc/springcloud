package com.springcloud.demo.Dao;

import com.springcloud.demo.Entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:23
 * @Description: 数据库操作实体类demo
 */
@Repository
public class DemoDao extends BaseDao {

    /**
     * 保存用户
     *
     * @param user
     */
    public void save(User user) {
        String sql = "insert into sys_user(id, userName, passWord) values(?,?,?)";
        jdbcTemplate.update(sql, user.getId(), user.getUserName(), user.getPassWord());
    }

    /**
     * 删除用户
     *
     * @param id
     */
    public void delete(String id) {
        String sql = "delete from sys_user where id=?";
        jdbcTemplate.update(sql, id);
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
