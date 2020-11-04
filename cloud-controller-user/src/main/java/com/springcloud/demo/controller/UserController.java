package com.springcloud.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: dq
 * @Date: 2020/11/3 13:49
 * @Description: 用户前端控制类
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {


    /**
     * 保存
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    public Map<String, Object> save() {
        Map<String, Object> resultMap = new HashMap();
        /*List<String> services = discoveryClient.getServices();
        resultMap.put("success",services.get(0));*/
        return resultMap;
       /* Map<String, Object> resultMap = new HashMap<>(16);

        userService.save(user);

        return resultMap;*/

    }
}
