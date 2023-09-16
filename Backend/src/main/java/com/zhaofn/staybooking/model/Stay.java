package com.zhaofn.staybooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.util.List;

@Entity//hibernate要求
@Table(name = "stay")
@JsonDeserialize(builder = Stay.Builder.class)
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String address;
    @JsonProperty("guest_number")
    private int guestNumber;
    @ManyToOne
    @JoinColumn(name = "user_id")//说明这个fk在另一个表里的名字
    private User host;


    @JsonIgnore
    @OneToMany(mappedBy = "stay", cascade = CascadeType.ALL, fetch=FetchType.LAZY)//mappedBy用于non-owning side，表明Date里面的stay field持有这个关系并用于map back回此table(因为在db里一般是date里边存是哪个stay的date，stay不会存所有date)
    private List<StayReservedDate> reservedDates;

    //OneToMany的关系写出来操作会更方便, 可以不写
    @OneToMany(mappedBy = "stay", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private List<StayImage> images;

    public Stay() {}

    private Stay(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.address = builder.address;
        this.guestNumber = builder.guestNumber;
        this.host = builder.host;
        this.reservedDates = builder.reservedDates;
        this.images = builder.images;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public User getHost() {
        return host;
    }

    public List<StayReservedDate> getReservedDates() {
        return reservedDates;
    }

    public List<StayImage> getImages() {
        return images;
    }

    public Stay setImages(List<StayImage> images) {
        this.images = images;
        return this;
    }

    public static class Builder {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("address")
        private String address;

        @JsonProperty("guest_number")
        private int guestNumber;

        @JsonProperty("host")
        private User host;

        @JsonProperty("dates")
        private List<StayReservedDate> reservedDates;

        @JsonProperty("images")
        private List<StayImage> images;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setGuestNumber(int guestNumber) {
            this.guestNumber = guestNumber;
            return this;
        }

        public Builder setHost(User host) {
            this.host = host;
            return this;
        }

        public Builder setReservedDates(List<StayReservedDate> reservedDates) {
            this.reservedDates = reservedDates;
            return this;
        }

        public Builder setImages(List<StayImage> images) {
            this.images = images;
            return this;
        }

        public Stay build() {
            return new Stay(this);
        }
    }
}

