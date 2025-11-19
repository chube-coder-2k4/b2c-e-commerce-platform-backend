package dev.commerce.services.impl;

import dev.commerce.dtos.request.UserRequest;
import dev.commerce.dtos.response.UserResponse;
import dev.commerce.entitys.Users;
import dev.commerce.repositories.UserRepository;
import dev.commerce.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public UUID saveUser(UserRequest request) throws MessagingException, UnsupportedEncodingException {
        Users user = Users.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        return user.getId();
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
        Users user = userRepository.findByEmail(email);
        if(user == null) {
            throw new RuntimeException("User not found");
        }
        if(user.isVerify()) {
            return "User already verified";
        }
        return null;
    }

    @Override
    public void deleteUser(long userId) {

    }

    @Override
    public Users findByEmail(String email) {
        return null;
    }

    @Override
    public Users findById(UUID userId) {
        return null;
    }
}
