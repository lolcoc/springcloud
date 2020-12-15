package com.springcloud.demo.entity;

import java.io.Serializable;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:23
 * @Description: 用户
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1851709879951162609L;
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
    /**
     * 身份证号
     */
    private String IDCard;

    /**
     * 手机号
     * @return
     */
    private String phoneNumber;

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

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
