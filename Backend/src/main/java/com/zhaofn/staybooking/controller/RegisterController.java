package com.zhaofn.staybooking.controller;

import com.zhaofn.staybooking.model.User;
import com.zhaofn.staybooking.model.UserRole;
import com.zhaofn.staybooking.service.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register/guest")
    public void addGuest(@RequestBody User user) {//通过名字查找对应数据来进行联系（优先@JsonProperty的名字）
        registerService.add(user, UserRole.ROLE_GUEST);
    }

    @PostMapping("/register/host")
    public void addHost(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_HOST);
    }
}