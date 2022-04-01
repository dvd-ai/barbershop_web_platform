package com.app.barbershopweb.order.crud.converter;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderConverterTest {

    private final OrderConverter orderConverter = new OrderConverter();
    private final OrderTestConstants otc = new OrderTestConstants();

    @Test
    void orderDtoListToEntityList() {

        List<Order> orders = otc.VALID_ORDER_DTO_LIST.stream()
                .map(dto -> new Order(
                        dto.orderId(),
                        dto.barbershopId(),
                        dto.barberId(),
                        dto.customerId(),
                        dto.orderDate(),
                        dto.active()
                ))
                .toList();

        assertEquals(
                orders,
                orderConverter.orderDtoListToEntityList(
                        otc.VALID_ORDER_DTO_LIST
                )
        );
    }

    @Test
    void orderEntityListToDtoList() {
        List<OrderDto> dtos = otc.VALID_ORDER_ENTITY_LIST.stream()
                .map(entity -> new OrderDto(
                        entity.getOrderId(),
                        entity.getBarbershopId(),
                        entity.getBarberId(),
                        entity.getCustomerId(),
                        entity.getOrderDate(),
                        entity.getActive()
                ))
                .toList();

        assertEquals(
                dtos,
                orderConverter.orderEntityListToDtoList(
                        otc.VALID_ORDER_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                otc.VALID_ORDER_ENTITY,
                orderConverter.mapToEntity(otc.VALID_ORDER_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                otc.VALID_ORDER_DTO,
                orderConverter.mapToDto(otc.VALID_ORDER_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(otc.ORDER_FIELD_AMOUNT, Order.class.getDeclaredFields().length);
    }
}
