package com.zhaofn.staybooking.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//jpa可以根据创建的entity来自动生成schema，并且支持onetomany和manytoone
@Entity
@Table(name = "authority")
public class Authority {

    @Id
    private String username;
    private String authority;

    public Authority() {}

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public Authority setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAuthority() {
        return authority;
    }

    public Authority setAuthority(String authority) {
        this.authority = authority;
        return this;
    }
}
