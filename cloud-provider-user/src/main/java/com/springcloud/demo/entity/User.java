package com.springcloud.demo.entity;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:23
 * @Description: 用户
 */
public class User {
    /**
     * id
     */
    private Integer id;

    /**
     * 同户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
