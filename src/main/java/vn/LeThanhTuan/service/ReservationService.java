package vn.LeThanhTuan.service;

import org.springframework.data.domain.Page;
import vn.LeThanhTuan.entity.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    Page<ReservationDto> findAll(String keyword, int currPage, int pageSize);

    ReservationDto reservations(ReservationDto reservationDto);

    ReservationDto changeReservationState(int id, String state);

    List<ReservationDto> findAllByUserAndStatus(int userId, String status);

    List<ReservationDto> findAllByStatusPendingAndAccepted(int userId);

    List<Object[]> getRevenueByMonth(int year);
}
