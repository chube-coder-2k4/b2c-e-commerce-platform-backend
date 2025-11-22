package dev.commerce.services.impl;

import dev.commerce.dtos.common.LoginType;
import dev.commerce.dtos.request.UserRequest;
import dev.commerce.entitys.Role;
import dev.commerce.entitys.Users;
import dev.commerce.exception.InvalidDataException;
import dev.commerce.repositories.jpa.RoleRepository;
import dev.commerce.repositories.jpa.UserRepository;
import dev.commerce.services.MailService;
import dev.commerce.services.OtpVerifyService;
import dev.commerce.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpVerifyService otpVerifyService;
    private final MailService mailService;
    private final RoleRepository roleRepository;


    @Override
    public UUID saveUser(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidDataException("Email already exists");
        }
        if (userRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new InvalidDataException("Username already exists");
        }
        Set<Role> role = roleRepository.findByNameIn(request.getRole());
        Users user = Users.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .provider(LoginType.LOCAL)
                .isVerify(false)
                .isActive(true)
                .isLocked(false)
                .roles(role)
                .build();
        Users savedUser = userRepository.save(user);
        String otp = otpVerifyService.generateOtp();
        otpVerifyService.saveOtp(request.getEmail(), otp);
        mailService.sendOtpMail(savedUser.getEmail(), otp);

        
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
