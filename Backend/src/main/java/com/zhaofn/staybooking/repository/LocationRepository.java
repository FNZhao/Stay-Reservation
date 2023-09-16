package com.zhaofn.staybooking.repository;

import com.zhaofn.staybooking.model.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends ElasticsearchRepository<Location, Long>, CustomLocationRepository {//这是一个interface 不能在里面实现api,所以借助了一个新的interface然后extends这个interface然后实现自己的api,这样locationRepo就可以使用自己的api

}
