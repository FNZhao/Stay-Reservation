package com.zhaofn.staybooking.repository;

import com.zhaofn.staybooking.model.Stay;
import com.zhaofn.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {//primary key的type如果写错的话，当调用api的时候会让你输入错误类型的参数而非Long类型的，比如会让你searchById(String id)而不是(Long id)

    List<Stay> findByHost(User user);//jpa 通过non-null fields来转换成sql语句进行query，会自动忽略null的fields

    Stay findByIdAndHost(Long id, User host);//都是Spring实现的

    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Long> ids, int guestNumber);

}

