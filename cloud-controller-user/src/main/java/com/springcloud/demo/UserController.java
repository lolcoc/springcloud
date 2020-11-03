package com.springcloud.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:49
 * @Description: 用户前端控制类
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    public Map<String, Object> save(User user) {

        Map<String, Object> resultMap = new HashMap<>(16);

        userService.save(user);

        return resultMap;

    }
}
