package com.api.auth;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest body, HttpServletResponse response) {
        Optional<User> userOptional = userService.findByEmail(body.getEmail());

        if (userOptional.isPresent()) {
            try {
                authenticate(body.getEmail(), body.getPassword());

                AuthenticationResponse authenticationResponse = authenticationService
                        .generateAuthenticationResponse(userOptional.get());

                response.addCookie(generateCookieRefreshToken(authenticationResponse.getRefreshToken()));

                return ResponseEntity.status(ApiConstant.STATUS_200).body(
                        ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(authenticationResponse)
                                .build());
            } catch (Exception e) {
                return ResponseEntity.status(ApiConstant.STATUS_400)
                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Wrong password")
                                .build());
            }
        }

        return ResponseEntity.status(ApiConstant.STATUS_400)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Email is available").build());
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
                    AuthenticationResponse authenticationResponse = authenticationService
                            .generateAuthenticationResponse(userOptional.get());
                    response.addCookie(generateCookieRefreshToken(authenticationResponse.getRefreshToken()));
                    authenticate(body.getEmail(), body.getPassword());
                    return ResponseEntity.status(ApiConstant.STATUS_201).body(
                            ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(authenticationService)
                                    .build());
                }
            }

            return ResponseEntity.status(ApiConstant.STATUS_500)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                            .data("Something went wrong").build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_400)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Email is available").build());
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile() {
        try {
            Optional<CustomUserDetails> userDetailsOptional = authenticationService.getUserDetails();
            if (userDetailsOptional.isPresent()) {
                ProfileResponse profileResponse = ProfileResponse.builder()
                        .fullName(userDetailsOptional.get().getUser().getFullName())
                        .email(userDetailsOptional.get().getUser().getEmail())
                        .imageUrl(userDetailsOptional.get().getUser().getImageUrl())
                        .id(userDetailsOptional.get().getUser().getId())
                        .build();

                return ResponseEntity.status(ApiConstant.STATUS_200).body(
                        ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(profileResponse)
                                .build());
            }

        } catch (Exception e) {
        }
        return ResponseEntity.status(ApiConstant.STATUS_401)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR).data("Something went wrong")
                        .build());
    }

    @PatchMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletResponse response,
            @CookieValue(ApiConstant.COOKIE_REFRESH_TOKEN) String refreshToken) {

        try {
            if (refreshToken != null) {
                String userEmail = jwtService.extractUsername(refreshToken);
                if (userEmail != null) {
                    AuthenticationResponse authenticationResponse = authenticationService
                            .generateAuthenticationResponse(userEmail);

                    response.addCookie(generateCookieRefreshToken(authenticationResponse.getRefreshToken()));

                    return ResponseEntity.status(ApiConstant.STATUS_200).body(
                            ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(authenticationResponse)
                                    .build());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(ApiConstant.STATUS_401)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                            .data("Unauthorized").build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong").build());
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

    public void authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password));
    }
}
