package com.van.mngr.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> usermap = this.userService.getUserByUsername(username);
        if (usermap == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(usermap.get("username"), usermap.get("password"),
                Arrays.asList(new SimpleGrantedAuthority(String.format("ROLE_%s", usermap.get("role").toUpperCase()))));
    }
}
