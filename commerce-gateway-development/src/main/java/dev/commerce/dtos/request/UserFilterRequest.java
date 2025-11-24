package dev.commerce.dtos.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterRequest {
    private String fullName;
    private String email;
    private String phone;
    private String username;
    private Boolean isVerify;
    private Boolean isActive;
    private Boolean isLocked;
    private Set<String> roles;
    private String provider;
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String sortBy = "id";
    private String sortDir = "asc";
}
