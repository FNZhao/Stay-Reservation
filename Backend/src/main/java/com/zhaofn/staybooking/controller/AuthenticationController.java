package com.zhaofn.staybooking.controller;

import com.zhaofn.staybooking.model.Token;
import com.zhaofn.staybooking.model.User;
import com.zhaofn.staybooking.model.UserRole;
import com.zhaofn.staybooking.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate/guest")
    public Token authenticateGuest(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_GUEST);
    }

    @PostMapping("/authenticate/host")
    public Token authenticateHost(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_HOST);
    }

}
