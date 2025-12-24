package dev.commerce.services;

import dev.commerce.dtos.response.AuditLogResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    AuditLogResponse getAuditLogById(UUID id);
    List<AuditLogResponse> getAllAuditLogs(String action, LocalDateTime start, LocalDateTime end);
    List<AuditLogResponse> getAuditLogsByUserId(UUID userId);
    void log(String action, String details);


}
