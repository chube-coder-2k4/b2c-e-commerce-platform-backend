package dev.commerce.dtos.response;

import dev.commerce.dtos.common.LoginType;
import dev.commerce.entitys.Role;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String username;
    private boolean isVerify;
    private boolean isActive;
    private boolean isLocked;
    private LoginType provider;
    private Set<Role> roles;
    private UUID createdBy;
    private UUID updatedBy;

}
