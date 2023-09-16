package com.zhaofn.staybooking.controller;

import com.zhaofn.staybooking.model.Reservation;
import com.zhaofn.staybooking.model.Stay;
import com.zhaofn.staybooking.model.User;
import com.zhaofn.staybooking.service.ReservationService;
import com.zhaofn.staybooking.service.StayService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
public class StayController {

    private final StayService stayService;
    private final ReservationService reservationService;

    public StayController(StayService stayService, ReservationService reservationService) {
        this.stayService = stayService;
        this.reservationService = reservationService;
    }

//    @GetMapping(value = "/stays")
//    public List<Stay> listStays(@RequestParam(name = "host") String hostName) {
//        return stayService.listByUser(hostName);
//    }

    @GetMapping(value = "/stays")
    public List<Stay> listStays(Principal principal) {
        return stayService.listByUser(principal.getName());
    }

    @GetMapping(value = "/stays/{stayId}")
    public Stay getStay(@PathVariable Long stayId, Principal principal) {//
        return stayService.findByIdAndHost(stayId, principal.getName());
    }


    @PostMapping("/stays")
    public void addStay(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("guest_number") int guestNumber,
            @RequestParam("images") MultipartFile[] images,//multipart和form-data是一个概念，之前还没有文件上传的时候用的json，现在需要上传文件则要改成multipart//文件的大小有限制，但是可以在application.properties里设置上限
            Principal principal) {


        Stay stay = new Stay.Builder()
                .setName(name)
                .setAddress(address)
                .setDescription(description)
                .setGuestNumber(guestNumber)
                .setHost(new User.Builder().setUsername(principal.getName()).build())
                .build();
        stayService.add(stay, images);
    }


    @DeleteMapping("/stays/{stayId}")
    public void deleteStay(@PathVariable Long stayId, Principal principal) {
        stayService.delete(stayId, principal.getName());
    }

    @GetMapping(value = "/stays/reservations/{stayId}")
    public List<Reservation> listReservations(@PathVariable Long stayId) {
        return reservationService.listByStay(stayId);
    }

}

