package com.zhaofn.staybooking.config;

import com.zhaofn.staybooking.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity//spring打开web security
public class SecurityConfig extends WebSecurityConfigurerAdapter{//Spring 3.0不需要extends，之前的需要

    private final DataSource dataSource;
    private final JwtFilter jwtFilter;


    public SecurityConfig(DataSource dataSource, JwtFilter jwtFilter) {
        this.dataSource = dataSource;
        this.jwtFilter = jwtFilter;
    }

    @Bean//Spring没有password encoder的源代码，所以要写一个bean然后new一个提供给spring
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/register/*").permitAll()
                .antMatchers(HttpMethod.POST, "/authenticate/*").permitAll()//这个也可以和上面那一行写在一起
                //.antMatchers("/stays").hasAuthority("ROLE_HOST")//.permitAll()
                .antMatchers("/stays/**").hasAuthority("ROLE_HOST")//.permitAll()//一个星号的话如果出现 stay/reservations/的话Guest也可以访问
                .antMatchers("/search").hasAuthority("ROLE_GUEST")
                //.antMatchers("/reservations").hasAuthority("ROLE_GUEST")
                .antMatchers("/reservations/**").hasAuthority("ROLE_GUEST")//两个星号是指所有以reservations开头的url必须要是guest
                .anyRequest().authenticated()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//不用session based的auth
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);//使用jwt
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//spring security需要的查询用户信息的部分
        auth.jdbcAuthentication().dataSource(dataSource)//告诉db
                .passwordEncoder(passwordEncoder())//告诉用的哪个encoder
                .usersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?")//spring会把问号换成username//告诉怎么知道用户名密码
                .authoritiesByUsernameQuery("SELECT username, authority FROM authority WHERE username = ?");
    }

    //**
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
