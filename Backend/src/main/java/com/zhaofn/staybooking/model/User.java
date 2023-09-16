package com.zhaofn.staybooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//这个Entity是hibernate的annotation，如果没有entity的话创建table会报错
@Table(name = "user")
@JsonDeserialize(builder = User.Builder.class)//因为json的定义都在builder里所以要写这一行
public class User {//user不能用record，因为hibernate与record不兼容,如果要用record的话要在下面创建一个空arg的constructor
    @Id//primary key
    private String username;
    @JsonIgnore//password和enable这两个信息不想让他们被提供回前端，所以加上@JsonIgnore
    private String password;
    @JsonIgnore
    private boolean enabled;

    public User() {}
    //这个是constructor,define了一个有args的constructor之后一定要写一个空arg的constructor
    private User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.enabled = builder.enabled;
    }
    //builder pattern用法，Builder定义在了下面
    //builder定义方法更加清晰
//    User user = new User.Builder()
//            .setUsername("user")
//            .setPassword("1234")
//            .setEnabled(true)
//            .build();

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {//这里没有用void，如果return自己的话就可以使用fluent API了: user.setUsername(user).setPassword(password)
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public static class Builder {
        @JsonProperty("username")
        private String username;

        @JsonProperty("password")
        private String password;

        @JsonProperty("enabled")
        private boolean enabled;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
