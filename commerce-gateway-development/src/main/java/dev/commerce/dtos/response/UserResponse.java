package dev.commerce.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private boolean isVerify;
    private boolean isActive;
    private boolean isLocked;
    private String provider;
    private String[] roles;

}
