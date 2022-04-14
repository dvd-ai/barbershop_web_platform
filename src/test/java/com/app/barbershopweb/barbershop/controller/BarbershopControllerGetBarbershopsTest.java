package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.BarbershopController;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_FIELD_AMOUNT;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing GET: " + BARBERSHOPS_URL)
@ExtendWith(MockitoExtension.class)
class BarbershopControllerGetBarbershopsTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter converter;

    @DisplayName("gives empty Barbershop List when they're no added barbershops yet")
    @Test
    void shouldReturnEmptyBarbershopList() throws Exception {

        when(barbershopService.getBarbershops()).thenReturn(Collections.emptyList());
        when(converter.barbershopEntityListToDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.
                perform(get(BARBERSHOPS_URL)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string("[]"));
    }

    @DisplayName("gives all barbershops at once")
    @Test
    void shouldReturnAllBarbershops() throws Exception {
        when(barbershopService.getBarbershops()).thenReturn(
                BARBERSHOP_VALID_ENTITY_LIST
        );
        when(converter.barbershopEntityListToDtoList(BARBERSHOP_VALID_ENTITY_LIST)).thenReturn(
                BARBERSHOP_VALID_DTO_LIST
        );

        MockHttpServletResponse response = mockMvc.
                perform(get(BARBERSHOPS_URL)).
                andDo(print()).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        checkBarbershopDtoJson(response.getContentAsString());
    }

    void checkBarbershopDtoJson(String json) {
        DocumentContext context = JsonPath.parse(json);
        List<Object> object = context.read("$");

        assertEquals(BARBERSHOP_VALID_DTO_LIST.size(), object.size());

        for (var i = 0; i < BARBERSHOP_VALID_DTO_LIST.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(BARBERSHOP_FIELD_AMOUNT, dto.size());
            assertEquals(BARBERSHOP_VALID_DTO_LIST.get(i).id().intValue(), (Integer) context.read("$[" + i + "].id"));
            assertEquals(BARBERSHOP_VALID_DTO_LIST.get(i).address(), context.read("$[" + i + "].address"));
            assertEquals(BARBERSHOP_VALID_DTO_LIST.get(i).name(), context.read("$[" + i + "].name"));
            assertEquals(BARBERSHOP_VALID_DTO_LIST.get(i).phoneNumber(), context.read("$[" + i + "].phoneNumber"));
            assertEquals(BARBERSHOP_VALID_DTO_LIST.get(i).email(), context.read("$[" + i + "].email"));
            assertEquals(BARBERSHOP_VALID_DTO_LIST.get(i).workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME), context.read("$[" + i + "].workTimeFrom"));
            assertEquals(BARBERSHOP_VALID_DTO_LIST.get(i).workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME), context.read("$[" + i + "].workTimeTo"));
        }
    }
}