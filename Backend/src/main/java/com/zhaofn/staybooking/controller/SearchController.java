package com.zhaofn.staybooking.controller;


import com.zhaofn.staybooking.exception.InvalidSearchDateException;
import com.zhaofn.staybooking.model.Stay;
import com.zhaofn.staybooking.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
public class SearchController {


    private final SearchService searchService;


    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping(value = "/search")
    public List<Stay> searchStays(
            @RequestParam(name = "guest_number") int guestNumber,
            @RequestParam(name = "checkin_date") String start,
            @RequestParam(name = "checkout_date") String end,
            @RequestParam(name = "lat") double lat,
            @RequestParam(name = "lon") double lon,
            @RequestParam(name = "distance", required = false) String distance) {//如果是null的话之前有定义过默认50//required=false，这一项不是必要的
        LocalDate checkinDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));//spring版本2.不能自动map，如果是3.则可以直接写成localdate，不需要此转换
        LocalDate checkoutDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        if (checkinDate.equals(checkoutDate) || checkinDate.isAfter(checkoutDate) || checkinDate.isBefore(LocalDate.now())) {
            throw new InvalidSearchDateException("Invalid date for search");
        }
        return searchService.search(guestNumber, checkinDate, checkoutDate, lat, lon, distance);
    }
}
