package com.springcloud.demo.test;

import com.springcloud.demo.util.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redis")
public class RedisTest {

    @RequestMapping(value = "/test" ,method = RequestMethod.GET)
    public String testRedis(){
        return "";
    }
}
