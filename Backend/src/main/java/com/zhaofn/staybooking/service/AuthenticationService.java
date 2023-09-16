package com.zhaofn.staybooking.service;

import com.zhaofn.staybooking.exception.UserNotExistException;
import com.zhaofn.staybooking.model.Token;
import com.zhaofn.staybooking.model.User;
import com.zhaofn.staybooking.model.UserRole;
import com.zhaofn.staybooking.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {//用于验证用户名与密码，然后就可以生成token

    //借助authentication manager来实现, 是spring boot提供的
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    public Token authenticate(User user, UserRole role) throws UserNotExistException {
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException exception) {
            throw new UserNotExistException("User Doesn't Exist");
        }

        if (auth == null || !auth.isAuthenticated() || !auth.getAuthorities().contains(new SimpleGrantedAuthority(role.name()))) {
            throw new UserNotExistException("User Doesn't Exist");
        }
        return new Token(jwtUtil.generateToken(user.getUsername()));
    }

}
