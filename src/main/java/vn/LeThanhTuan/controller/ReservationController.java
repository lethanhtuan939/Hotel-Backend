package vn.LeThanhTuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.LeThanhTuan.entity.dto.ReservationDto;
import vn.LeThanhTuan.entity.dto.RoomDto;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAll(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                  @RequestParam(value = "p", defaultValue = "1") int pageIndex,
                                                  @RequestParam(value = "s", defaultValue = "5") int pageSize) {
        Page<ReservationDto> page = reservationService.findAll(keyword, pageIndex, pageSize);
        List<ReservationDto> reservationDtos = page.getContent();
        int totalPages = page.getTotalPages();

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(reservationDtos)
                .keyword(keyword)
                .currentPage(pageIndex)
                .totalPages(totalPages)
                .pageSize(pageSize)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{user}")
    public ResponseEntity<ResponseObject> findAllByUserAndStatus(@PathVariable int user, @RequestParam("status") String status) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(reservationService.findAllByUserAndStatus(user, status))
                .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> reservations(@RequestBody ReservationDto reservationDto) {
        System.out.println(reservationDto);
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully!")
                .data(reservationService.reservations(reservationDto))
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/state")
    public ResponseEntity<ResponseObject> changeReservationState(@RequestParam("id") int id, @RequestParam("state") String state) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(reservationService.changeReservationState(id, state))
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{user}/current")
    public ResponseEntity<ResponseObject> findAllByUserAndStatus(@PathVariable int user) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(reservationService.findAllByStatusPendingAndAccepted(user))
                .build(), HttpStatus.OK);
    }
}
