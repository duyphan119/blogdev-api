package com.api.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.role.Role;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> resultUser = userService.findByEmail(username);
        if (resultUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = resultUser.get();

        Collection<GrantedAuthority> grantedAuthoritySet = new HashSet<>();

        Set<Role> roles = user.getRoles();

        for (Role accountRole : roles) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority(accountRole.getName()));
        }
        return new CustomUserDetails(grantedAuthoritySet, user);
    }

}
