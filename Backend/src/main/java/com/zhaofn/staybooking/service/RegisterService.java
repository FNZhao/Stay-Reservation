package com.zhaofn.staybooking.service;

import com.zhaofn.staybooking.exception.UserAlreadyExistException;
import com.zhaofn.staybooking.model.Authority;
import com.zhaofn.staybooking.model.User;
import com.zhaofn.staybooking.model.UserRole;
import com.zhaofn.staybooking.repository.AuthorityRepository;
import com.zhaofn.staybooking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service//这个annotation告诉Spring这是一个service
public class RegisterService {
    private final UserRepository userRepository;//因为定义了final，所以必须要被initialize，需要在constructor里inject
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    //要确保所有的constructor都可以initialize上面两个变量，所以如果写另一个空的constructor还会报错
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //在一个constructor里call另一个constructor要用this()

    @Transactional//(isolation = Isolation.SERIALIZABLE)//有多个写操作需要加transactional, isolation就是指当transition的时候外界读取时读到的是什么时候的值，一般default就可以
    public void add(User user, UserRole role) throws UserAlreadyExistException {
        if (userRepository.existsById(user.getUsername())) {//existsById是jpa里自带的
            throw new UserAlreadyExistException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));//把密码加密，因为设置了bcrypt
        user.setEnabled(true);//Spring security系统里的，如果不enable就用不了这个用户

        //上一个项目使用的user detail manager自动存的
        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));//ENUM.name()是built-in，enum存到表里的时候存的是string
    }
}
