package vn.LeThanhTuan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.LeThanhTuan.entity.dto.HotelDto;
import vn.LeThanhTuan.entity.dto.RoomDto;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.HotelService;
import vn.LeThanhTuan.service.RoomService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    @Autowired
    HotelService hotelService;

    @Autowired
    RoomService roomService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAll(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                  @RequestParam(value = "p", defaultValue = "1") int pageIndex,
                                                  @RequestParam(value = "s", defaultValue = "5") int pageSize) {
        Page<HotelDto> page = hotelService.findAllHotels(keyword, pageIndex, pageSize);
        List<HotelDto> hotelDtos = page.getContent();
        int totalPages = page.getTotalPages();

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .data(hotelDtos)
                .totalPages(totalPages)
                .currentPage(pageIndex)
                .pageSize(pageSize)
                .keyword(keyword)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> findAllList(@RequestParam(value = "keyword", defaultValue = "") String keyword) {

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .data(hotelService.findAllList(keyword))

                .keyword(keyword)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable int id) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .data(hotelService.findById(id))
                .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createHotel(@RequestParam("data") String data,
                                                      @RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
        HotelDto hotelDto = new ObjectMapper().readValue(data, HotelDto.class);

        HotelDto savedHotel = hotelService.createHotel(hotelDto, file);

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully")
                .data(savedHotel)
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateHotel(@RequestParam("data") String data,
                                                      @RequestParam(name = "file", required = false) MultipartFile file,
                                                      @PathVariable int id) throws IOException {

        HotelDto hotelDto = new ObjectMapper().readValue(data, HotelDto.class);

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .data(hotelService.updateHotel(hotelDto, file, id))
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}/room")
    public ResponseEntity<ResponseObject> findRoomByHotel(@PathVariable("id") int hotelId,
                                                          @RequestParam(value = "p", defaultValue = "1") int pageIndex,
                                                          @RequestParam(value = "s", defaultValue = "5") int pageSize) {
        Page<RoomDto> page = roomService.findRoomByHotel(hotelId, pageIndex, pageSize);
        List<RoomDto> roomDtos = page.getContent();

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .data(roomDtos)
                .totalPages(page.getTotalPages())
                .currentPage(pageIndex)
                .pageSize(pageSize)
                .build(), HttpStatus.OK);
    }
}
