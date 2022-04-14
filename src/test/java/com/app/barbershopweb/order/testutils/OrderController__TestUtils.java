package com.app.barbershopweb.order.testutils;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderDto;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderController__TestUtils {
    
    private OrderController__TestUtils() {}
    
    public static void checkOrderDtoJson(String json, List<OrderDto> orderDtos, boolean customerIdIsNull) {
        DocumentContext context = JsonPath.parse(json);
        List<Object> object = context.read("$");

        assertEquals(orderDtos.size(), object.size());

        for (var i = 0; i < orderDtos.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(ORDER_FIELD_AMOUNT, dto.size());
            assertEquals(orderDtos.get(i).orderId().intValue(), (Integer) context.read("$[" + i + "].orderId"));
            assertEquals(orderDtos.get(i).barbershopId().intValue(), (Integer) context.read("$[" + i + "].barbershopId"));
            assertEquals(orderDtos.get(i).barberId().intValue(), (Integer) context.read("$[" + i + "].barberId"));

            if (customerIdIsNull) {
                assertEquals(orderDtos.get(i).customerId(), context.read("$[" + i + "].customerId"));
            } else assertEquals(orderDtos.get(i).customerId().intValue(), (Integer) context.read("$[" + i + "].customerId"));

            assertEquals(orderDtos.get(i).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), context.read("$[" + i + "].orderDate"));
            assertEquals(orderDtos.get(i).active(), context.read("$[" + i + "].active"));
        }
    }

    public static void checkOrderEntityJson(String json, List<Order> orders, boolean customerIdIsNull) {
        DocumentContext context = JsonPath.parse(json);
        List<Object> object = context.read("$");

        assertEquals(orders.size(), object.size());

        for (var i = 0; i < orders.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(ORDER_FIELD_AMOUNT, dto.size());
            assertEquals(orders.get(i).getOrderId().intValue(), (Integer) context.read("$[" + i + "].orderId"));
            assertEquals(orders.get(i).getBarbershopId().intValue(), (Integer) context.read("$[" + i + "].barbershopId"));
            assertEquals(orders.get(i).getBarberId().intValue(), (Integer) context.read("$[" + i + "].barberId"));

            if (customerIdIsNull) {
                assertEquals(orders.get(i).getCustomerId(), context.read("$[" + i + "].customerId"));
            } else assertEquals(orders.get(i).getCustomerId().intValue(), (Integer) context.read("$[" + i + "].customerId"));

            assertEquals(orders.get(i).getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), context.read("$[" + i + "].orderDate"));
            assertEquals(orders.get(i).getActive(), context.read("$[" + i + "].active"));
        }
    }
    
}
