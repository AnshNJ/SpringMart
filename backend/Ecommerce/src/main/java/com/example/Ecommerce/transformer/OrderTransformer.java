package com.example.Ecommerce.transformer;

import com.example.Ecommerce.dto.response.OrderResponseDto;
import com.example.Ecommerce.model.Ordered;
import jakarta.persistence.criteria.Order;

public class OrderTransformer {
    public static OrderResponseDto OrderToOrderResponseDto(Ordered order){
        return OrderResponseDto.builder()
                .orderNo(order.getOrderNo())
                .orderDate(order.getOrderDate())
                .totalValue(order.getTotalValue())
                .cardUsed(order.getCardUsed())
                .build();
    }
}
