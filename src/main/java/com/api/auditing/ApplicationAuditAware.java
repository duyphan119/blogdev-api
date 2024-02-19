package com.api.auditing;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.api.auth.AuthenticationService;
import com.api.user.CustomUserDetails;

public class ApplicationAuditAware implements AuditorAware<Long> {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        CustomUserDetails userDetails = authenticationService.getUserDetails().get();
        return Optional.ofNullable(userDetails.getUser().getId());
    }

}
