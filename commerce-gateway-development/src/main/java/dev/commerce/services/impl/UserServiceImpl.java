package dev.commerce.services.impl;

import dev.commerce.dtos.common.LoginType;
import dev.commerce.dtos.request.UserRequest;
import dev.commerce.entitys.Role;
import dev.commerce.entitys.Users;
import dev.commerce.exception.InvalidDataException;
import dev.commerce.repositories.jpa.RoleRepository;
import dev.commerce.repositories.jpa.UserRepository;
import dev.commerce.services.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UUID saveUser(UserRequest request) throws MessagingException, UnsupportedEncodingException {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidDataException("Email already exists");
        }
        
        // Check if username (email) already exists
        if (userRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new InvalidDataException("Username already exists");
        }
        

        
        // Build user entity
        Users user = Users.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getEmail()) // Use email as username
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .provider(LoginType.LOCAL)
                .isVerify(false) // New users need to verify email
                .isActive(true)
                .isLocked(false)
                .roles(null) // Default role
                .build();
        
        // Save user to database
        Users savedUser = userRepository.save(user);
        
        // TODO: Send verification email
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        return savedUser.getId();
    }


    @Override
    public UUID updateUser(UUID userId, UserRequest request) {
        Users user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        return user.getId();

    }

    @Override
    public String confirmUser(String email, String verifyCode) {
        Users user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
        if(user == null) {
            throw new RuntimeException("User not found");
        }
        if(user.isVerify()) {
            return "User already verified";
        }
        return null;
    }

    @Override
    public void deleteUser(UUID userId) {

    }

    @Override
    public Users findByEmail(String email) {
        return null;
    }

    @Override
    public Users findById(UUID userId) {
        return null;
    }

    //findByUsername
    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
