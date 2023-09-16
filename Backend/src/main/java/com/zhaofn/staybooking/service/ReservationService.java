package com.zhaofn.staybooking.service;


import com.zhaofn.staybooking.exception.ReservationCollisionException;
import com.zhaofn.staybooking.exception.ReservationNotFoundException;
import com.zhaofn.staybooking.model.*;
import com.zhaofn.staybooking.repository.ReservationRepository;
import com.zhaofn.staybooking.repository.StayReservationDateRepository;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StayReservationDateRepository stayReservationDateRepository;


    public ReservationService(ReservationRepository reservationRepository, StayReservationDateRepository stayReservationDateRepository) {
        this.reservationRepository = reservationRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
    }


    public List<Reservation> listByGuest(String username) {
        //return reservationRepository.findByGuest_Username(username);//这一个方法更好，因为下面创建的user里只有username，创建的数据并不完整,是个逻辑上有错误的数据
        return reservationRepository.findByGuest(new User.Builder().setUsername(username).build());
    }


    public List<Reservation> listByStay(Long stayId) {
        // return reservationRepository.findByStay_Id(stayId);
        return reservationRepository.findByStay(new Stay.Builder().setId(stayId).build());
    }


    @Transactional//如果这里写了transaction则dp repo每一个api都会transaction， 如果写了的话就在这一层进行一个总的transaction
    public void add(Reservation reservation) throws ReservationCollisionException {
        Set<Long> stayIds = stayReservationDateRepository.findByIdInAndDateBetween(
                List.of(reservation.getStay().getId()),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate().minusDays(1)
        );
        if (!stayIds.isEmpty()) {
            throw new ReservationCollisionException("Duplicate reservation");
        }


        List<StayReservedDate> reservedDates = new ArrayList<>();
        LocalDate start = reservation.getCheckinDate();
        LocalDate end = reservation.getCheckoutDate();
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            StayReservedDateKey id = new StayReservedDateKey(reservation.getStay().getId(), date);
            StayReservedDate reservedDate = new StayReservedDate(id, reservation.getStay());
            reservedDates.add(reservedDate);
        }
        stayReservationDateRepository.saveAll(reservedDates);
        reservationRepository.save(reservation);
    }


    @Transactional
    public void delete(Long reservationId, String username) {
        //Reservation reservation = reservationRepository.findByIdAndGuest_Username(reservationId, username);
        Reservation reservation = reservationRepository.findByIdAndGuest(reservationId, new User.Builder().setUsername(username).build());
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation is not available");
        }
        LocalDate start = reservation.getCheckinDate();
        LocalDate end = reservation.getCheckoutDate();
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            stayReservationDateRepository.deleteById(new StayReservedDateKey(reservation.getStay().getId(), date));
        }
        reservationRepository.deleteById(reservationId);
    }
}

