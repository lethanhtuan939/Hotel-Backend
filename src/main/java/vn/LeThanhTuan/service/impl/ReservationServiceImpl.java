package vn.LeThanhTuan.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.LeThanhTuan.common.Constant;
import vn.LeThanhTuan.common.DateUtil;
import vn.LeThanhTuan.entity.Hotel;
import vn.LeThanhTuan.entity.Reservation;
import vn.LeThanhTuan.entity.Room;
import vn.LeThanhTuan.entity.User;
import vn.LeThanhTuan.entity.dto.ReservationDto;
import vn.LeThanhTuan.entity.id.RoomId;
import vn.LeThanhTuan.exception.ResourceNotFoundException;
import vn.LeThanhTuan.repository.HotelRepository;
import vn.LeThanhTuan.repository.ReservationRepository;
import vn.LeThanhTuan.repository.RoomRepository;
import vn.LeThanhTuan.repository.UserRepository;
import vn.LeThanhTuan.service.ReservationService;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    public ReservationDto toDto(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDto.class);
    }

    public Reservation toReservation(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        try {
            reservation.setBookingDate(DateUtil.formatToDate(reservationDto.getBookingDate()));
            reservation.setDayStart(DateUtil.formatToDate(reservationDto.getDayStart()));
            reservation.setDayEnd(DateUtil.formatToDate(reservationDto.getDayEnd()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        reservation.setPaymentMethod(reservationDto.getPaymentMethod());
        reservation.setPrice(reservationDto.getPrice());
        reservation.setNotes(reservationDto.getNotes());

        return reservation;
    }

    @Override
    public Page<ReservationDto> findAll(String keyword, int currPage, int pageSize) {
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(currPage-1, pageSize, sort);
        Page<Reservation> page;

        if(keyword.trim().isEmpty()) {
            page = reservationRepository.findAll(pageable);
        } else {
            page = reservationRepository.findAllByKeyword(keyword, pageable);
        }

        return page.map(this::toDto);
    }

    @Override
    public ReservationDto reservations(ReservationDto reservationDto) {
        Hotel hotel = hotelRepository.findById(reservationDto.getRoom().getHotel().getId()).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", reservationDto.getRoom().getHotel().getId()));
        Room room = roomRepository.findById(new RoomId(reservationDto.getRoom().getId(), hotel)).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", reservationDto.getRoom().getId()));
        room.setHotel(hotel);
        room.setState(Constant.ROOM_STATE_PLACED);
        User user = userRepository.findByEmail(reservationDto.getUser().getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "email", reservationDto.getUser().getEmail()));
        Reservation reservation = toReservation(reservationDto);

        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setStatus(Constant.STATUS_PENDING);
        Reservation saved = reservationRepository.save(reservation);

        return toDto(saved);
    }

    @Override
    public ReservationDto changeReservationState(int id, String state) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        reservation.setStatus(state);

        if(state.equals(Constant.STATUS_DONE) || state.equals(Constant.STATUS_CANCEL)) {
            reservation.getRoom().setState(Constant.ROOM_STATE_EMPTY);
        }

        return toDto(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationDto> findAllByUserAndStatus(int userId, String status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Reservation> reservations = reservationRepository.findAllByUserAndStatus(user, status);

        return reservations.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> findAllByStatusPendingAndAccepted(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Reservation> reservations = reservationRepository.findAllBStatusPendingAndAccepted(user, Constant.STATUS_PENDING, Constant.STATUS_ACCEPTED);

        return reservations.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Object[]> getRevenueByMonth(int year) {
        return reservationRepository.findRevenueByMonth(year);
    }
}
