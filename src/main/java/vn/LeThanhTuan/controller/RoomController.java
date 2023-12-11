package vn.LeThanhTuan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.LeThanhTuan.entity.dto.RoomDto;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.RoomService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAllRooms(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                       @RequestParam(value = "p", defaultValue = "1") int pageIndex,
                                                       @RequestParam(value = "s", defaultValue = "5") int pageSize) {
        Page<RoomDto> page = roomService.findAllRooms(keyword, pageIndex, pageSize);
        List<RoomDto> roomDtos = page.getContent();
        int totalPages = page.getTotalPages();

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully")
                .data(roomDtos)
                .totalPages(totalPages)
                .currentPage(pageIndex)
                .pageSize(pageSize)
                .keyword(keyword)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/room")
    public ResponseEntity<ResponseObject> findRoomById(@RequestParam("rid") int rid, @RequestParam("hid") int hid) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(roomService.findById(rid, hid))
                .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createRoom(@RequestParam("data") String data,
                                                     @RequestParam("file") MultipartFile file) throws IOException {

        RoomDto roomDto = new ObjectMapper().readValue(data, RoomDto.class);
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully!")
                .data(roomService.createRoom(roomDto, file))
                .build(), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseObject> updateRoomById(@RequestParam("rid") int rid, @RequestParam("hid") int hid,
                                                         @RequestParam("data") String data,
                                                         @RequestParam(name = "file", required = false) MultipartFile file) throws IOException {

        RoomDto roomDto = new ObjectMapper().readValue(data, RoomDto.class);
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(roomService.updateRoom(rid, hid, roomDto, file))
                .build(), HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<ResponseObject> findRoomTypes() {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(roomService.findRoomTypes())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseObject> findAvailableRooms( @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                              @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(roomService.filterByDate(startDate, endDate))
                .build(), HttpStatus.OK);
    }
}
