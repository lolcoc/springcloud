package com.springcloud.demo.controller;

import com.springcloud.demo.entity.User;
import com.springcloud.demo.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "保存用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "user", required = true, dataType = "User")
    })
    @ResponseBody
    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    public Map<String, Object> save(@RequestBody User user) {
        Map<String, Object> resultMap = new HashMap();
        userService.save(user);
        return resultMap;
    }

    /**
     * 查询
     * @param
     * @return
     */
    @ApiOperation(value = "查询所有用户")
    @ResponseBody
    @RequestMapping(value = "/find" ,method = RequestMethod.GET)
    public Map<String, Object> findAll() {

        System.out.println("------->>user");

        Map<String, Object> resultMap = new HashMap<>(16);
        userService.findAll();
        return resultMap;

    }
}
