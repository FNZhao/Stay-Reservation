package com.zhaofn.staybooking.service;

import com.zhaofn.staybooking.exception.StayDeleteException;
import com.zhaofn.staybooking.exception.StayNotExistException;
import com.zhaofn.staybooking.model.*;
import com.zhaofn.staybooking.repository.LocationRepository;
import com.zhaofn.staybooking.repository.ReservationRepository;
import com.zhaofn.staybooking.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class StayService {

    private final ImageStorageService imageStorageService;
    private final StayRepository stayRepository;
    private final GeoCodingService geoCodingService;
    private final LocationRepository locationRepository;
    private final ReservationRepository reservationRepository;


    public StayService(ImageStorageService imageStorageService, StayRepository stayRepository, GeoCodingService geoCodingService, LocationRepository locationRepository, ReservationRepository reservationRepository) {
        this.imageStorageService = imageStorageService;
        this.stayRepository = stayRepository;
        this.geoCodingService = geoCodingService;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Stay> listByUser(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());//**
    }

    public Stay findByIdAndHost(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        return stay;
    }

    @Transactional
    public void add(Stay stay, MultipartFile[] images) {
        List<StayImage> stayImages = Arrays.stream(images)
                .filter(image -> !image.isEmpty())
                .parallel()
                .map(image -> imageStorageService.save(image))//也可以写成.map(imageStorageService::save), 使用method reference，因为image进去放save里只有一个arg，只能这么做
                .map(mediaLink -> new StayImage(mediaLink, stay))//这个不能用method reference，因为stayImage里有两个args
                .toList();//.collect(Collectors.toList());
        stay.setImages(stayImages);

        stayRepository.save(stay);

        Location location = geoCodingService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);
    }

    public void delete(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }

        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(stay, LocalDate.now());
        if (reservations != null && !reservations.isEmpty()) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }

        stayRepository.deleteById(stayId);
    }
}