package com.api.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.jwt.JwtService;
import com.api.user.CustomUserDetails;
import com.api.user.User;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Override
    public Optional<CustomUserDetails> getUserDetails() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return Optional.of(userDetails);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public AuthenticationResponse generateAuthenticationResponse(User user) {
        CustomUserDetails userDetails = CustomUserDetails.builder().user(user).build();
        var accessToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken(accessToken).refreshToken(refreshToken)
                .accessTokenExpired(jwtService.getJwtExpiration()).build();
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse generateAuthenticationResponse(String email) {
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .user(User.builder().email(email).build()).build();
        var accessToken = jwtService.generateToken(userDetails);
        var newRefreshToken = jwtService.generateRefreshToken(userDetails);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken(accessToken).refreshToken(newRefreshToken)
                .accessTokenExpired(jwtService.getJwtExpiration()).build();
        return authenticationResponse;
    }

}
