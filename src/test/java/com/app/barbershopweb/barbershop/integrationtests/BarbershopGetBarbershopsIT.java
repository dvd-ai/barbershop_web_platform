package com.app.barbershopweb.barbershop.integrationtests;


import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BarbershopGetBarbershopsIT extends AbstractIT{

    @Autowired
    private JdbcBarbershopRepository barbershopRepository;

    @Test
    void shouldReturnAlLastNames() {
        Barbershop barbershop = new Barbershop(
                1L, "a1",
                "", "+38091",
                "1@gmail.com"
        );

        barbershopRepository.addBarbershop(barbershop);

        assertEquals(barbershop.getId(), barbershopRepository.findBarbershopById(1L).get().getId());
        assertEquals(barbershop.getAddress(), barbershopRepository.findBarbershopById(1L).get().getAddress());

    }

}
