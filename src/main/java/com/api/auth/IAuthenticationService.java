package com.api.auth;

import java.util.Optional;

import com.api.user.CustomUserDetails;
import com.api.user.User;

public interface IAuthenticationService {
    public Optional<CustomUserDetails> getUserDetails();

    public AuthenticationResponse generateAuthenticationResponse(User user);

    public AuthenticationResponse generateAuthenticationResponse(String email);
}
