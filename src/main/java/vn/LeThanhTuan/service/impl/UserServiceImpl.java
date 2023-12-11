package vn.LeThanhTuan.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.LeThanhTuan.common.FileUtil;
import vn.LeThanhTuan.entity.Role;
import vn.LeThanhTuan.entity.User;
import vn.LeThanhTuan.entity.dto.UserDto;
import vn.LeThanhTuan.exception.ResourceNotFoundException;
import vn.LeThanhTuan.repository.LocationRepository;
import vn.LeThanhTuan.repository.RoleRepository;
import vn.LeThanhTuan.repository.UserRepository;
import vn.LeThanhTuan.request.RegisterRequest;
import vn.LeThanhTuan.request.UserRequest;
import vn.LeThanhTuan.response.AuthResponse;
import vn.LeThanhTuan.response.ChangePasswordRequest;
import vn.LeThanhTuan.service.JwtService;
import vn.LeThanhTuan.service.UserService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromAddress;


    private User toUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
    private User toUser(RegisterRequest user) {
        return modelMapper.map(user, User.class);
    }

    private AuthResponse validateAccount(RegisterRequest userDTO){
        if(ObjectUtils.isEmpty(userDTO)) {
            return AuthResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failures!")
                    .build();
        }

        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());

        if(user.isPresent()) {
            return AuthResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failures!")
                    .build();
        }
        return AuthResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Valid!")
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        AuthResponse auth = validateAccount(request);

        if(auth.getCode() == HttpStatus.BAD_REQUEST.value()) {
            return auth;
        }

        User user = toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER"));
        user.setRoles(roles);
        user.setEnabled(true);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                            .code(HttpStatus.OK.value())
                            .message("Register successfully!")
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .build();
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return toDto(user);
    }

    @Override
    public UserDto findUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return toDto(user);
    }

    @Override
    public AuthResponse login(UserRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isEmpty()) {
            return AuthResponse.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Email không đúng!")
                    .build();
        }
        User user = optionalUser.get();

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Mật khẩu nhập không chính xác!")
                    .build();
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .code(HttpStatus.OK.value())
                            .message("Login successfully!")
                            .build();
    }

    @Override
    public List<UserDto> fetchAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto, MultipartFile file) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if(file != null) {
            String fileName = FileUtil.generatedFileName(file);
            user.setImage(fileName);
            FileUtil.saveFile(fileName, file);
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setLocation(userDto.getLocation());
        user.setRoles(userDto.getRoles());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEnabled(userDto.isEnabled());

        User updatedUser = userRepository.save(user);

        return toDto(updatedUser);
    }

    @Override
    public AuthResponse changePassword(ChangePasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isEmpty()) {
            return AuthResponse.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Email không đúng!")
                    .build();
        }
        User user = optionalUser.get();

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            return AuthResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Mật khẩu nhập lại phải giống với mật khẩu!")
                    .build();
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return AuthResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Đổi mật khẩu thành công!")
                .build();
    }

    private int generateCode() {
        Random random = new Random();
        int min = 100_000;
        int max = 999_999;

        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public int sendOTPMail(String email) throws UnsupportedEncodingException, MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String senderName = "Hotel Reservation";
        String subject = "Xác thực tài khoản";
        String content = "Mã xác thực của bạn là: [[code]]";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        int code = generateCode();

        helper.setFrom(fromAddress, senderName);
        helper.setTo(email);
        helper.setSubject(subject);

        content = content.replace("[[code]]", String.valueOf(code));

        helper.setText(content);

        mailSender.send(message);

        return code;
    }

}
