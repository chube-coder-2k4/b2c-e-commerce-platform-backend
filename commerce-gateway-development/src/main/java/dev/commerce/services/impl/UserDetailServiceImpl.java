package dev.commerce.services.impl;

import dev.commerce.entitys.Users;
import dev.commerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userService.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    @Transactional
    public UserDetails loadUserById(UUID id) throws UsernameNotFoundException {
        Users user = userService.findById(id);
        if(user == null){
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        return user;
    }
}
