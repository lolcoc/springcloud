/*
package com.springcloud.demo.test;

import com.springcloud.demo.util.RedisStringUtil;
import com.springcloud.demo.util.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/redis")
public class RedisTest {
    @RequestMapping(value = "/test" ,method = RequestMethod.GET)
    public Map<String,Object> testRedis(){
        Map returnMap = new HashMap();
        RedisStringUtil.setEx("123",500,10,TimeUnit.SECONDS);
        byte[] dump = RedisUtil.dump("123");
        RedisUtil.restore("234",dump,60, TimeUnit.SECONDS);
        returnMap.put("1",RedisStringUtil.get("123"));
        returnMap.put("2",dump);
        returnMap.put("3",RedisUtil.getExpire("234"));
        returnMap.put("4",RedisStringUtil.get("234"));
        return returnMap;
    }


    @RequestMapping(value = "get" , method = RequestMethod.GET)
    public String getAll( ){
        Object o = RedisStringUtil.get("123");
        long expire = RedisUtil.getExpire("123");
        Object t = RedisStringUtil.get("234");
        long texpire = RedisUtil.getExpire("234");
        return "123 :" + o + " " + expire  + "\t"+ "234 :" + t + "  " + texpire;
    }
}
*/
