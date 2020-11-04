package com.springcloud.demo.controller;

import com.springcloud.demo.entity.User;
import com.springcloud.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:49
 * @Description: 用户前端控制类
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Resource
    private UserService userService;
    /**
     * 保存
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    public Map<String, Object> save(@PathVariable("user") User user) {
        Map<String, Object> resultMap = new HashMap();
        userService.save(user);
        return resultMap;
    }

    /**
     * 查询
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("/find")
    public Map<String, Object> findAll() {

        System.out.println("22222222");

        Map<String, Object> resultMap = new HashMap<>(16);
        userService.findAll();
        return resultMap;

    }
}
