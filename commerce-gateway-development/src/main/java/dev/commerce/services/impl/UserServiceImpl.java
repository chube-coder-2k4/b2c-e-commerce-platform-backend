package dev.commerce.services.impl;

import dev.commerce.dtos.common.LoginType;
import dev.commerce.dtos.request.UserFilterRequest;
import dev.commerce.dtos.request.UserRequest;
import dev.commerce.dtos.request.UserUpdateRequest;
import dev.commerce.dtos.response.UserResponse;
import dev.commerce.entitys.Role;
import dev.commerce.entitys.Users;
import dev.commerce.exception.InvalidDataException;
import dev.commerce.mappers.UsersMapper;
import dev.commerce.repositories.jpa.RoleRepository;
import dev.commerce.repositories.jpa.UserRepository;
import dev.commerce.services.MailService;
import dev.commerce.services.OtpVerifyService;
import dev.commerce.services.UserService;
import dev.commerce.utils.AuthenticationUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
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
    private final AuthenticationUtils utils;
    private final UsersMapper usersMapper;


    @Override
    @Transactional(rollbackFor = Exception.class) // rollbackFor là để chỉ các lệ cụ thể để rollback
    public UUID saveUser(UserRequest request) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidDataException("Email already exists");
        }
        if (userRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new InvalidDataException("Username already exists");
        }
        Set<Role> role = roleRepository.findByNameIn(request.getRole());
        Users user = usersMapper.toEntity(request);
        user.setRoles(role);
        user.setCreatedBy(utils.getCurrentUserId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Users savedUser = userRepository.save(user);
        String otp = otpVerifyService.generateOtp();
        otpVerifyService.saveOtp(request.getEmail(), otp);
        mailService.sendOtpMail(savedUser.getEmail(), otp);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser.getId();
    }


    @Override
    public UUID updateUser(UUID userId, UserUpdateRequest request) {
        Users user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        user.setFullName(request.getFullName());
        if(!user.getEmail().equals(request.getEmail())) {
            if(userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new InvalidDataException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setUpdatedBy(utils.getCurrentUserId());
        userRepository.save(user);
        log.info("User updated successfully with ID: {}", user.getId());
        return user.getId();
    }


    @Override
    public void deleteUser(UUID userId) {
        Users user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        log.info("User deleted successfully with ID: {}", userId);
    }

    @Override
    public Users findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Users findById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


    @Override
    public Page<UserResponse> getAllUserWithFilter(UserFilterRequest filterRequest) {

        Specification<Users> spec = Specification.allOf();

        spec = containsIgnoreCase(spec, "fullName", filterRequest.getFullName());
        spec = containsIgnoreCase(spec, "email", filterRequest.getEmail());
        spec = containsIgnoreCase(spec, "phone", filterRequest.getPhone());
        spec = containsIgnoreCase(spec, "username", filterRequest.getUsername());

        spec = equalsIfNotNull(spec, "isVerify", filterRequest.getIsVerify());
        spec = equalsIfNotNull(spec, "isActive", filterRequest.getIsActive());
        spec = equalsIfNotNull(spec, "isLocked", filterRequest.getIsLocked());
        spec = equalsIfNotNull(spec, "provider", filterRequest.getProvider());

        if (filterRequest.getRoles() != null && !filterRequest.getRoles().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    root.join("roles")
                            .get("name")
                            .in(filterRequest.getRoles())
            );
        }

        Sort sort = filterRequest.getSortDir().equalsIgnoreCase("asc") ?
                Sort.by(filterRequest.getSortBy()).ascending() :
                Sort.by(filterRequest.getSortBy()).descending();

        Pageable pageable = PageRequest.of(filterRequest.getPageNo(), filterRequest.getPageSize(), sort);
        return userRepository.findAll(spec, pageable)
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .username(user.getUsername())
                        .isVerify(user.isVerify())
                        .isActive(user.isActive())
                        .isLocked(user.isLocked())
                        .provider(user.getProvider())
                        .address(user.getAddress())
                        .roles(user.getRoles())
                        .build()
                );
    }



    private Specification<Users> containsIgnoreCase(
            Specification<Users> spec, String field, String value) {
        if (value == null || value.isEmpty()) return spec;
        return spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%")
        );
    }

    private <T> Specification<Users> equalsIfNotNull(
            Specification<Users> spec, String field, T value) {
        if (value == null) return spec;
        return spec.and((root, query, cb) ->
                cb.equal(root.get(field), value)
        );
    }




}
