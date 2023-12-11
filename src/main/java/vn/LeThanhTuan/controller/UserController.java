package vn.LeThanhTuan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
   import vn.LeThanhTuan.entity.dto.UserDto;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<ResponseObject> fetchAllUsers() {

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(userService.fetchAllUsers())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findUserById(@PathVariable int id) {

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(userService.findUserById(id))
                .build(), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> findUserByEmail(@RequestParam("e") String email) {

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(userService.findUserByEmail(email))
                .build(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<ResponseObject> findAllRoles() {
        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(userService.findAllRoles())
                .build(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable int id, @RequestParam(name = "data") String data, @RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
        UserDto userDto = new ObjectMapper().readValue(data, UserDto.class);

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully!")
                .data(userService.updateUser(id, userDto, file))
                .build(), HttpStatus.OK);
    }
}
