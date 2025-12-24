package dev.commerce.services.impl;

import dev.commerce.dtos.response.AuditLogResponse;
import dev.commerce.entitys.AuditLog;
import dev.commerce.entitys.Users;
import dev.commerce.mappers.AuditLogMapper;
import dev.commerce.repositories.jpa.AuditLogRepository;
import dev.commerce.services.AuditLogService;
import dev.commerce.utils.AuthenticationUtils;
import dev.commerce.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final AuthenticationUtils utils;
    private final MessageUtils messageUtils;

    @Override
    public AuditLogResponse getAuditLogById(UUID id) {
        AuditLog auditlog = auditLogRepository.findById(id).orElse(null);
        if(auditlog != null){
            return auditLogMapper.toAuditLogResponse(auditlog);
        }
        else {
            throw new RuntimeException(messageUtils.toLocale("audit.notfound"));
        }
    }

    @Override
    public List<AuditLogResponse> getAllAuditLogs(String action, LocalDateTime start, LocalDateTime end) {
        Specification<AuditLog> spec = (root,query,cr) -> cr.conjunction();
        if(action != null && !action.isEmpty()){
            spec = spec.and((root,query,cr) -> cr.like(cr.lower(root.get("action")), "%" + action.toLowerCase() + "%"));
        }
        if(start != null){
            spec = spec.and((root,query,cr) -> cr.greaterThanOrEqualTo(root.get("createdAt"), start));
        }
        if(end != null){
            spec = spec.and((root,query,cr) -> cr.lessThanOrEqualTo(root.get("createdAt"), end));
        }
        return auditLogRepository.findAll(spec).stream()
                .map(auditLogMapper::toAuditLogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByUserId(UUID userId) {
        return auditLogRepository.findByCreatedBy(userId).stream()
                .map(auditLogMapper::toAuditLogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void log(String action, String details) {
        Users us = utils.getCurrentUser();
        AuditLog auditLog = AuditLog.builder()
                .users(us)
                .action(action)
                .details(details)
                .build();
        auditLog.setCreatedBy(us.getId());
        auditLog.setUpdatedBy(us.getId());
        auditLogRepository.save(auditLog);
    }
}
