package com.zhaofn.staybooking.model;

import javax.persistence.*;

@Entity
@Table(name = "stay_reserved_date")
public class StayReservedDate {

    @EmbeddedId
    private StayReservedDateKey id;
    @MapsId("stay_id")//foreign key，表明embeddedId里面的stay id也作为一个fk
    @ManyToOne//这个是决定的，大部分情况是manytoone这一边，如果是另一边，则需要写mappedby
    private Stay stay;

    public StayReservedDate() {
    }

    public StayReservedDate(StayReservedDateKey id, Stay stay) {
        this.id = id;
        this.stay = stay;
    }

    public StayReservedDateKey getId() {
        return id;
    }

    public Stay getStay() {
        return stay;
    }
}