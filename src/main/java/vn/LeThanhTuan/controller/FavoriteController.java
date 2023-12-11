package vn.LeThanhTuan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.FavoriteService;

@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<ResponseObject> findListFavorite(@RequestParam("user") int userId) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(favoriteService.findListFavorite(userId))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addFavorite(@RequestParam("user") int userId, @RequestParam("hotel") int hotelId) {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(favoriteService.addFavorite(userId, hotelId))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseObject> removeFavorite(@RequestParam("user") int userId, @RequestParam("hotel") int hotelId) {
        favoriteService.removeFavorite(userId, hotelId);
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteByIdFavorite(@RequestParam("id") int id) {
        favoriteService.removeFavorite(id);
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .build(), HttpStatus.OK);
    }
}
