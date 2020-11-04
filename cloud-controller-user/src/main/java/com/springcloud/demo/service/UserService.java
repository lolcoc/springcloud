package com.springcloud.demo.service;

import com.springcloud.demo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="CLOUD-PROVIDER-USER")
public interface UserService {

    @PostMapping("/save")
    void save(User user);
}
