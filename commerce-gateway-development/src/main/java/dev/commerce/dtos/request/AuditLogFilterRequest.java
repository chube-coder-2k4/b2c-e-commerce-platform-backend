package dev.commerce.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AuditLogFilterRequest {
    private UUID userId;
    private String action;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}