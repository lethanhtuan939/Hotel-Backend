package vn.LeThanhTuan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HomeController {
    @Autowired
    ReservationService reservationService;

    @GetMapping("/revenue")
    public ResponseEntity<ResponseObject> findRevenueByYear(@RequestParam("year") int year) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(reservationService.getRevenueByMonth(year))
                .build(), HttpStatus.OK);
    }
}
