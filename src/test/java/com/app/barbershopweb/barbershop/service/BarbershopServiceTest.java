package com.app.barbershopweb.barbershop.service;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopService;
import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarbershopServiceTest {

    @Mock
    BarbershopRepository barbershopRepository;

    @InjectMocks
    BarbershopService barbershopService;

    BarbershopTestConstants btc = new BarbershopTestConstants();

    @Test
    void addBarbershop() {
        when(barbershopRepository.addBarbershop(any()))
                .thenReturn(btc.VALID_BARBERSHOP_ID);

        Long id = barbershopService.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);

        assertEquals(btc.VALID_BARBERSHOP_ID, id);
    }

    @Test
    void deleteBarbershopById() {
        barbershopService.deleteBarbershopById(btc.VALID_BARBERSHOP_ID);
        verify(barbershopRepository, times(1)).deleteBarbershopById(any());
    }

    @Test
    void updateBarbershop() {
        when(barbershopRepository.updateBarbershop(any()))
                .thenReturn(Optional.of(btc.VALID_UPDATED_BARBERSHOP_ENTITY));

        Optional<Barbershop> barbershopUpdOptional = barbershopService
                .updateBarbershop(btc.VALID_UPDATED_BARBERSHOP_ENTITY);

        assertTrue(barbershopUpdOptional.isPresent());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_ENTITY, barbershopUpdOptional.get());
    }

    @Test
    void findBarbershopById() {
        when(barbershopRepository.findBarbershopById(any()))
                .thenReturn(Optional.of(btc.VALID_BARBERSHOP_ENTITY));

        Optional<Barbershop> foundBarbershopOpt = barbershopService
                .findBarbershopById(btc.VALID_BARBERSHOP_ID);

        assertTrue(foundBarbershopOpt.isPresent());
        assertEquals(btc.VALID_BARBERSHOP_ENTITY, foundBarbershopOpt.get());
    }

    @Test
    void getBarbershops() {
        when(barbershopRepository.getBarbershops())
                .thenReturn(btc.VALID_BARBERSHOP_ENTITY_LIST);

        List<Barbershop> barbershops = barbershopService.getBarbershops();

        assertEquals(btc.VALID_BARBERSHOP_ENTITY_LIST.size(), barbershops.size());
        assertEquals(btc.VALID_BARBERSHOP_ENTITY_LIST, barbershops);
    }
}