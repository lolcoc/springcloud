package com.springcloud.demo.controller;

import com.springcloud.demo.entity.User;
import com.springcloud.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:49
 * @Description: 用户前端控制类
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 保存
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Map<String, Object> save(@RequestBody User user) {

        Map<String, Object> resultMap = new HashMap<>(16);
        System.out.println(user.getUserName());
        userService.save(user);

        return resultMap;

    }

    /**
     * 查询
     * @param
     * @return
     */
    @RequestMapping("/find")
    public Map<String, Object> findAll() {

        System.out.println("------>provider");

        Map<String, Object> resultMap = new HashMap<>(16);
        List<User> list = userService.findAll();
        resultMap.put("list",list);
        return resultMap;

    }

    /**
     * 登录
     * @param userName
     * @param passWord
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String, Object> login(String userName, String passWord) {
        Map<String, Object> resultMap = new HashMap<>(16);
        User user = userService.selectUserByName(userName);
        boolean flag = false;
        if (user != null){
            flag = user.getPassWord().equals(passWord);
        }
        if (flag){
            resultMap.put("success",user);
            return resultMap;
        }else {
            resultMap.put("error","用户名或密码错误");
        }
        return resultMap;

    }


    /**
     * 注册
     * @param IDCard
     * @param phoneNumber
     * @param userName
     * @param passWord
     * @return
     */
    @RequestMapping("/register")
    public Map<String, Object> register(String IDCard, String phoneNumber ,String userName, String passWord) {
        Map<String, Object> resultMap = new HashMap<>(16);
        User user = userService.selectUserByIDCard(IDCard);
        if (user != null) {
            resultMap.put("error", "该身份证已注册过");
            return resultMap;
        }
        user = userService.selectUserByphoneNumber(phoneNumber);
        if (user != null) {
            resultMap.put("error", "该手机号已注册过");
            return resultMap;
        }
        user = userService.selectUserByName(userName);
        if (user != null) {
            resultMap.put("error", "该用户名已使用");
            return resultMap;
        }
        user.setPhoneNumber(phoneNumber);
        user.setUserName(userName);
        user.setPassWord(passWord);
        user.setIDCard(IDCard);
        userService.save(user);
        resultMap.put("success",user);
        return resultMap;

    }
}
