package com.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.api.jwt.JwtAuthenticationFilter;
import com.api.role.RoleName;
import com.api.user.CustomUserDetailsService;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private JwtAuthenticationFilter jwtAuthFilter;

        @Autowired
        private AuthenticationProvider authenticationProvider;

        @Autowired
        private LogoutHandler logoutHandler;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                return httpSecurity
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(GET, "/category",
                                                                "/category/*", "/category-parent",
                                                                "/category-parent/*",
                                                                "/article",
                                                                "/article/slug/*",
                                                                "/article/id/*",
                                                                "/article/author/*",
                                                                "/web/**", "/article-comment", "/article-reply-comment",
                                                                "/article/recommend/slug/*")
                                                .permitAll()
                                                .requestMatchers(POST, "/auth/login",
                                                                "/auth/register", "/upload/**", "/subscriber",
                                                                "/contact")
                                                .permitAll()
                                                .requestMatchers(PATCH, "/auth/refresh-token")
                                                .permitAll()
                                                // Role Admin
                                                .requestMatchers(GET, "/subscriber", "/role", "/contact")
                                                .hasAnyAuthority(RoleName.ADMIN.name())
                                                .requestMatchers(POST, "/category", "/category-parent", "/article")
                                                .hasAnyAuthority(RoleName.ADMIN.name())
                                                .requestMatchers(PATCH, "/category/*", "/category-parent/*",
                                                                "/article/*")
                                                .hasAnyAuthority(RoleName.ADMIN.name())
                                                .requestMatchers(DELETE, "/category/*", "/category-parent/*",
                                                                "/article/*", "/subscriber/*",
                                                                "/contact/*")
                                                .hasAnyAuthority(RoleName.ADMIN.name())
                                                // Role Admin && user
                                                .requestMatchers(GET, "/auth/profile", "/article/author")
                                                .hasAnyAuthority(RoleName.ADMIN.name(), RoleName.USER.name())
                                                .requestMatchers(POST, "/article-comment", "/article-reply-comment",
                                                                "/article/author")
                                                .hasAnyAuthority(RoleName.ADMIN.name(), RoleName.USER.name())
                                                .requestMatchers(PATCH, "/article-comment/*",
                                                                "/article-reply-comment/*", "/auth/profile",
                                                                "/article/author/*")
                                                .hasAnyAuthority(RoleName.ADMIN.name(), RoleName.USER.name())
                                                .requestMatchers(DELETE, "/article-comment/*",
                                                                "/article-reply-comment/*", "/article/author/*")
                                                .hasAnyAuthority(RoleName.ADMIN.name(), RoleName.USER.name())
                                                .anyRequest()
                                                .authenticated()
                                //
                                )
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout.logoutUrl("/auth/logout")
                                                .addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler((request, response,
                                                                authentication) -> SecurityContextHolder
                                                                                .clearContext()))
                                .build();
        }

        @Bean
        BCryptPasswordEncoder encryptPassword() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(username -> userDetailsService.loadUserByUsername(username));
                authProvider.setPasswordEncoder(encryptPassword());
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
