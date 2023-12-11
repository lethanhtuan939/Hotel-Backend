package vn.LeThanhTuan.service;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;
import vn.LeThanhTuan.entity.Role;
import vn.LeThanhTuan.entity.dto.UserDto;
import vn.LeThanhTuan.request.RegisterRequest;
import vn.LeThanhTuan.request.UserRequest;
import vn.LeThanhTuan.response.AuthResponse;
import vn.LeThanhTuan.response.ChangePasswordRequest;
import vn.LeThanhTuan.response.ResponseObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    AuthResponse register(RegisterRequest userDto);

    UserDto findUserByEmail(String email);

    UserDto findUserById(int id);

    AuthResponse login(UserRequest request);

    List<UserDto> fetchAllUsers();

    List<Role> findAllRoles();

    UserDto updateUser(int id, UserDto userDto, MultipartFile file) throws IOException;

    AuthResponse changePassword(ChangePasswordRequest request);

    int sendOTPMail(String email) throws UnsupportedEncodingException, MessagingException;
}
