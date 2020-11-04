package com.springcloud.demo.service;

import com.springcloud.demo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value="CLOUD-PROVIDER-USER")
public interface UserService {

    @RequestMapping("/user/save")
    void save(User user);
}
