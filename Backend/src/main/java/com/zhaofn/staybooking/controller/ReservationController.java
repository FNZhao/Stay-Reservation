package com.zhaofn.staybooking.controller;


import com.zhaofn.staybooking.exception.InvalidReservationDateException;
import com.zhaofn.staybooking.model.Reservation;
import com.zhaofn.staybooking.model.User;
import com.zhaofn.staybooking.service.ReservationService;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


@RestController
public class ReservationController {
    private final ReservationService reservationService;


    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping(value = "/reservations")//"value ="可以不写
    public List<Reservation> listReservations(Principal principal) {
        return reservationService.listByGuest(principal.getName());
    }


    @PostMapping("/reservations")
    public void addReservation(@RequestBody Reservation reservation, Principal principal) {
        LocalDate checkinDate = reservation.getCheckinDate();
        LocalDate checkoutDate = reservation.getCheckoutDate();
        if (!checkinDate.isBefore(checkoutDate) || checkinDate.isBefore(LocalDate.now())) {
            throw new InvalidReservationDateException("Invalid date for reservation");
        }
        reservation.setGuest(new User.Builder().setUsername(principal.getName()).build());
        reservationService.add(reservation);
    }


    @DeleteMapping("/reservations/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId, Principal principal) {
        reservationService.delete(reservationId, principal.getName());
    }
}
