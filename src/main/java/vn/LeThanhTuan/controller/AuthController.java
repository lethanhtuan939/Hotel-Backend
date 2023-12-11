package vn.LeThanhTuan.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.LeThanhTuan.entity.dto.UserDto;
import vn.LeThanhTuan.request.RegisterRequest;
import vn.LeThanhTuan.request.UserRequest;
import vn.LeThanhTuan.response.AuthResponse;
import vn.LeThanhTuan.response.ChangePasswordRequest;
import vn.LeThanhTuan.response.ResponseObject;
import vn.LeThanhTuan.service.UserService;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    UserService userService;

    int code;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest request) {

        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<ResponseObject> sendOtp(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {

        code = userService.sendOTPMail(email);

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("Check your email!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseObject> verify(@RequestParam("otp") int otp) throws MessagingException, UnsupportedEncodingException {

       if(code == otp) {
           return new ResponseEntity<>(ResponseObject.builder()
                   .code(HttpStatus.OK.value())
                   .message("Thay đổi mật khẩu của bạn")
                   .build(), HttpStatus.OK);
       }

        return new ResponseEntity<>(ResponseObject.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Mã OTP không chính xác!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePassword(@RequestBody ChangePasswordRequest request) throws MessagingException, UnsupportedEncodingException {

        return new ResponseEntity<>(userService.changePassword(request), HttpStatus.OK);
    }
}
