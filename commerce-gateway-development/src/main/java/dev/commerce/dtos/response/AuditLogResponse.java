package dev.commerce.dtos.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogResponse(UUID id,
                               String username,
                               String action,
                               String details,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt,
                               UUID createdBy,
                               UUID updatedBy) {}
