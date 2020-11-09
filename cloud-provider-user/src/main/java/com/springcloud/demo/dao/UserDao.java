package com.springcloud.demo.dao;

import com.springcloud.demo.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    /**
     * 根据用户名查询单个用户
     * @param userName
     * @return
     */
    public User selectUserByName(String userName) {
        String sql = "select * from sys_user where user_name like ?";
        return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                User user = null;
                if (rs.next()){
                    user =new User();
                    user.setId(rs.getInt("id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPassWord(rs.getString("pass_word"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                }
                return user;
            }
        }, userName);
    }

    public User selectUserByIDCard(String idCard) {
        String sql = "select * from sys_user where user_name like ?";
        return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                User user = null;
                if (rs.next()){
                    user =new User();
                }
                return user;
            }
        }, idCard);
    }

    public User selectUserByphoneNumber(String phoneNumber) {
        String sql = "select * from sys_user where user_name like ?";
        return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                User user = null;
                if (rs.next()){
                    user =new User();
                    user.setId(rs.getInt("id"));
                }
                return user;
            }
        }, phoneNumber);
    }
}
