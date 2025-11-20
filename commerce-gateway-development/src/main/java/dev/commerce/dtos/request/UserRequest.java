package dev.commerce.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String roleName;
    private String provider;
}
