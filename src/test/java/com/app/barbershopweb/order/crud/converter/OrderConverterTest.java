package com.app.barbershopweb.order.crud.converter;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_VALID_DTO;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_DTO_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderConverterTest {

    private final OrderConverter orderConverter = new OrderConverter();


    @Test
    void orderDtoListToEntityList() {

        List<Order> orders = ORDER_VALID_DTO_LIST.stream()
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
                        ORDER_VALID_DTO_LIST
                )
        );
    }

    @Test
    void orderEntityListToDtoList() {
        List<OrderDto> dtos = ORDER_VALID_ENTITY_LIST.stream()
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
                        ORDER_VALID_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                ORDER_VALID_ENTITY,
                orderConverter.mapToEntity(ORDER_VALID_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                ORDER_VALID_DTO,
                orderConverter.mapToDto(ORDER_VALID_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(ORDER_FIELD_AMOUNT, Order.class.getDeclaredFields().length);
    }
}
