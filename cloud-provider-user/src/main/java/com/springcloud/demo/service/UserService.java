package com.springcloud.demo.service;

import com.springcloud.demo.entity.User;
import org.springframework.web.bind.annotation.PostMapping;

public interface UserService {

    @PostMapping("/save")
    void save(User user);
}
