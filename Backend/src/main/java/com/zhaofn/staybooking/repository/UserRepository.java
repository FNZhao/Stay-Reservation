package com.zhaofn.staybooking.repository;

import com.zhaofn.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//jpa是一个规范，是hibernate提出的，比jdbc更高级一些
@Repository
public interface UserRepository extends JpaRepository<User, String> {//JpaRepository最终也是extends了CrudRepository, 也能自动实现page，sort
}
