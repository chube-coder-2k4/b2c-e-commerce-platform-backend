package dev.commerce.mappers;

import dev.commerce.dtos.response.AuditLogResponse;
import dev.commerce.entitys.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    @Mapping(source = "users.username", target = "username")
    AuditLogResponse toAuditLogResponse(AuditLog auditLog);
    List<AuditLogResponse> toAuditLogResponses(List<AuditLog> auditLogs);
}
