package com.api.auth;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.jwt.JwtService;
import com.api.role.Role;
import com.api.role.RoleName;
import com.api.role.RoleService;
import com.api.user.CustomUserDetails;
import com.api.user.User;
import com.api.user.UserService;
import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest body, HttpServletResponse response) {
        Optional<User> userOptional = userService.findByEmail(body.getEmail());

        if (userOptional.isPresent()) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                body.getEmail(),
                                body.getPassword()));

                User user = userOptional.get();
                CustomUserDetails userDetails = CustomUserDetails.builder().user(user).build();
                var accessToken = jwtService.generateToken(userDetails);
                var refreshToken = jwtService.generateRefreshToken(userDetails);
                AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken).refreshToken(refreshToken)
                        .accessTokenExpired(jwtService.getJwtExpiration()).build();

                response.addCookie(generateCookieRefreshToken(refreshToken));
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                body.getEmail(),
                                body.getPassword()));
                return ResponseEntity.status(ApiConstant.STATUS_200).body(
                        ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(authenticationResponse)
                                .build());
            } catch (Exception e) {
                return ResponseEntity.status(ApiConstant.STATUS_400)
                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Mật khẩu chưa chính xác")
                                .build());
            }
        }

        return ResponseEntity.status(ApiConstant.STATUS_400)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Email này chưa được đăng ký").build());
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest body, HttpServletResponse response) {
        Optional<User> userOptional = userService.findByEmail(body.getEmail());

        if (userOptional.isEmpty()) {
            long totalUsers = userService.countAll();
            Set<Role> roles = new HashSet<>();
            Role roleUser = null;
            if (totalUsers == 0) {
                roleUser = findRoleOrCreate(RoleName.ADMIN.name());
            } else {
                roleUser = findRoleOrCreate(RoleName.USER.name());
            }

            User user = User.builder()
                    .email(body.getEmail())
                    .hashedPassword(passwordEncoder.encode(body.getPassword()))
                    .firstName(body.getFirstName())
                    .lastName(body.getLastName())
                    .build();

            if (roleUser != null) {
                roles.add(roleUser);
                user.setRoles(roles);
            }
            if (!user.getRoles().isEmpty()) {
                userOptional = userService.create(user);

                if (userOptional.isPresent()) {
                    user = userOptional.get();
                    CustomUserDetails userDetails = CustomUserDetails.builder().user(user).build();
                    var accessToken = jwtService.generateToken(userDetails);
                    var refreshToken = jwtService.generateRefreshToken(userDetails);
                    AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken).refreshToken(refreshToken)
                            .accessTokenExpired(jwtService.getJwtExpiration()).build();

                    response.addCookie(generateCookieRefreshToken(refreshToken));
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    body.getEmail(),
                                    body.getPassword()));
                    return ResponseEntity.status(ApiConstant.STATUS_201).body(
                            ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(authenticationResponse)
                                    .build());
                }
            }

            return ResponseEntity.status(ApiConstant.STATUS_500)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                            .data("Có lỗi xảy ra trong quá trình đăng ký, vui lòng thử lại sau").build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_400)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Email này đã được đăng ký").build());
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile() {
        System.out.println("-----profile------");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            ProfileResponse profileResponse = ProfileResponse.builder()
                    .fullName(userDetails.getUser().getFullName())
                    .email(userDetails.getUser().getEmail())
                    .imageUrl(userDetails.getUser().getImageUrl())
                    .id(userDetails.getUser().getId())
                    .build();

            return ResponseEntity.status(ApiConstant.STATUS_200).body(
                    ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(profileResponse)
                            .build());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(ApiConstant.STATUS_401)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Something went wrong")
                            .build());
        }
    }

    @PatchMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletResponse response,
            @CookieValue(ApiConstant.COOKIE_REFRESH_TOKEN) String refreshToken) {

        try {
            if (refreshToken != null) {
                String userEmail = jwtService.extractUsername(refreshToken);
                if (userEmail != null) {
                    CustomUserDetails userDetails = CustomUserDetails.builder()
                            .user(User.builder().email(userEmail).build()).build();
                    var accessToken = jwtService.generateToken(userDetails);
                    var newRefreshToken = jwtService.generateRefreshToken(userDetails);
                    AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken).refreshToken(newRefreshToken)
                            .accessTokenExpired(jwtService.getJwtExpiration()).build();

                    response.addCookie(generateCookieRefreshToken(newRefreshToken));

                    return ResponseEntity.status(ApiConstant.STATUS_200).body(
                            ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(authenticationResponse)
                                    .build());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(ApiConstant.STATUS_401)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                            .data("Chưa đăng nhập").build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Có lỗi xảy ra trong quá trình đăng ký, vui lòng thử lại sau").build());
    }

    public Role findRoleOrCreate(String name) {
        Role role = null;
        Optional<Role> roleOptional = roleService.findByName(name);
        if (roleOptional.isPresent()) {
            role = roleOptional.get();
        } else {
            roleOptional = roleService.create(Role.builder().name(name).build());
            if (roleOptional.isPresent()) {
                role = roleOptional.get();
            }
        }
        return role;
    }

    public Cookie generateCookieRefreshToken(String refreshToken) {
        Cookie cookie = new Cookie(ApiConstant.COOKIE_REFRESH_TOKEN, refreshToken);
        cookie.setMaxAge((int) jwtService.getRefreshExpiration() / 1000);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        return cookie;
    }
}
