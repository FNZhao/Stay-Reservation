package com.zhaofn.staybooking.service;


import com.zhaofn.staybooking.model.Stay;
import com.zhaofn.staybooking.repository.LocationRepository;
import com.zhaofn.staybooking.repository.StayRepository;
import com.zhaofn.staybooking.repository.StayReservationDateRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class SearchService {
    private final StayRepository stayRepository;
    private final StayReservationDateRepository stayReservationDateRepository;
    private final LocationRepository locationRepository;


    public SearchService(StayRepository stayRepository, StayReservationDateRepository stayReservationDateRepository, LocationRepository locationRepository) {
        this.stayRepository = stayRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
        this.locationRepository = locationRepository;
    }


    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance) {
        List<Long> stayIds = locationRepository.searchByDistance(lat, lon, distance);
        if (stayIds == null || stayIds.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> reservedStayIds = stayReservationDateRepository.findByIdInAndDateBetween(stayIds, checkinDate, checkoutDate.minusDays(1));
        List<Long> filteredStayIds = stayIds.stream()
                .filter(stayId -> !reservedStayIds.contains(stayId))
                .collect(Collectors.toList());//这里没有用parallel()，因为之前的是上传用作，耗时很长，而判断contains耗时很短，创建线程花费时间更多
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);
    }
}
