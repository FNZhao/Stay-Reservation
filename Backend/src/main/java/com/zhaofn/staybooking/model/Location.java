package com.zhaofn.staybooking.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import javax.persistence.Id;

@Document(indexName = "loc")//elasticsearch的annotation,创建了一个搜索空间，对应sql里table的概念，index搜索空间里每一个数据就是document
public class Location {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @GeoPointField
    private GeoPoint geoPoint;//位置信息，里面包括经纬度


    public Location(Long id, GeoPoint geoPoint) {
        this.id = id;
        this.geoPoint = geoPoint;
    }


    public Long getId() {
        return id;
    }


    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

}
