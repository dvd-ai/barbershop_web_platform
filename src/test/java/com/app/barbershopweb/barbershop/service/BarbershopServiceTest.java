package com.app.barbershopweb.barbershop.service;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopService;
import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_UPDATED_ENTITY;
import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarbershopServiceTest {

    @Mock
    BarbershopRepository barbershopRepository;

    @InjectMocks
    BarbershopService barbershopService;


    @Test
    void addBarbershop() {
        when(barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY))
                .thenReturn(BARBERSHOP_VALID_BARBERSHOP_ID);

        Long id = barbershopService.addBarbershop(BARBERSHOP_VALID_ENTITY);

        assertEquals(BARBERSHOP_VALID_BARBERSHOP_ID, id);
    }

    @Test
    void deleteBarbershopById() {
        barbershopService.deleteBarbershopById(BARBERSHOP_VALID_BARBERSHOP_ID);
        verify(barbershopRepository, times(1)).deleteBarbershopById(BARBERSHOP_VALID_BARBERSHOP_ID);
    }

    @Test
    void updateBarbershop() {
        when(barbershopRepository.updateBarbershop(BARBERSHOP_VALID_UPDATED_ENTITY))
                .thenReturn(Optional.of(BARBERSHOP_VALID_UPDATED_ENTITY));

        Optional<Barbershop> barbershopUpdOptional = barbershopService
                .updateBarbershop(BARBERSHOP_VALID_UPDATED_ENTITY);

        assertTrue(barbershopUpdOptional.isPresent());
        assertEquals(BARBERSHOP_VALID_UPDATED_ENTITY, barbershopUpdOptional.get());
    }

    @Test
    void findBarbershopById() {
        when(barbershopRepository.findBarbershopById(BARBERSHOP_VALID_BARBERSHOP_ID))
                .thenReturn(Optional.of(BARBERSHOP_VALID_ENTITY));

        Optional<Barbershop> foundBarbershopOpt = barbershopService
                .findBarbershopById(BARBERSHOP_VALID_BARBERSHOP_ID);

        assertTrue(foundBarbershopOpt.isPresent());
        assertEquals(BARBERSHOP_VALID_ENTITY, foundBarbershopOpt.get());
    }

    @Test
    void getBarbershops() {
        when(barbershopRepository.getBarbershops())
                .thenReturn(BARBERSHOP_VALID_ENTITY_LIST);

        List<Barbershop> barbershops = barbershopService.getBarbershops();

        assertEquals(BARBERSHOP_VALID_ENTITY_LIST.size(), barbershops.size());
        assertEquals(BARBERSHOP_VALID_ENTITY_LIST, barbershops);
    }
}