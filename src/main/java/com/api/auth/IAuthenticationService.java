package com.api.auth;

import java.util.Optional;

import com.api.user.CustomUserDetails;
import com.api.user.User;

public interface IAuthenticationService {
    Optional<CustomUserDetails> getUserDetails();

    AuthenticationResponse generateAuthenticationResponse(User user);

    AuthenticationResponse generateAuthenticationResponse(String email);

    boolean isAdmin();
}
