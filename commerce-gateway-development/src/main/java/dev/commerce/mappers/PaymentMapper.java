package dev.commerce.mappers;

import dev.commerce.dtos.response.PaymentResponse;
import dev.commerce.entitys.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "orders.id", target = "orderId")
    PaymentResponse toResponse(Payment payment);
}

