package vn.LeThanhTuan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.LocationService;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAll() {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(locationService.findAll())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findLocationById(@PathVariable int id) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(locationService.findById(id))
                .build(), HttpStatus.OK);
    }
}
