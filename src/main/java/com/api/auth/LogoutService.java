package com.api.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.api.utils.ApiConstant;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    Cookie cookie = new Cookie(ApiConstant.COOKIE_REFRESH_TOKEN, "");
    cookie.setMaxAge(0);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    response.addCookie(cookie);

    System.out.println("Logout");

    SecurityContextHolder.clearContext();
  }

}
