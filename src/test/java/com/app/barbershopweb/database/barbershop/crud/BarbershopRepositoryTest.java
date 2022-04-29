package com.app.barbershopweb.database.barbershop.crud;

import com.app.barbershopweb.barbershop.crud.Barbershop;
import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_UPDATED_ENTITY;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static org.junit.jupiter.api.Assertions.*;

class BarbershopRepositoryTest extends AbstractIT {

    @Autowired
    BarbershopRepository barbershopRepository;


    @Test
    void addBarbershop() {
        Long id = barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        assertTrue(id > 0);
    }

    @Test
    void findBarbershopByExistedId() {
        Long id = barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        Optional<Barbershop> barbershopOptional = barbershopRepository.findBarbershopById(id);

        assertTrue(barbershopOptional.isPresent());
        assertEquals(BARBERSHOP_VALID_ENTITY, barbershopOptional.get());
    }

    @Test
    void findBarbershopByNotExistingId() {
        Optional<Barbershop> barbershopOptional = barbershopRepository.findBarbershopById(
                BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID
        );

        assertTrue(barbershopOptional.isEmpty());
    }

    @Test
    void updateExistingBarbershop() {
        barbershopRepository.addBarbershop(
                BARBERSHOP_VALID_ENTITY
        );

        Optional<Barbershop> updBarbershopOptional = barbershopRepository.updateBarbershop(
                BARBERSHOP_VALID_UPDATED_ENTITY
        );

        assertTrue(updBarbershopOptional.isPresent());
        assertEquals(BARBERSHOP_VALID_UPDATED_ENTITY, updBarbershopOptional.get());
    }

    @Test
    void updateNotExistingBarbershop() {
        Optional<Barbershop> updBarbershopOptional = barbershopRepository.updateBarbershop(
                BARBERSHOP_VALID_UPDATED_ENTITY
        );

        assertTrue(updBarbershopOptional.isEmpty());
    }

    @Test
    void getBarbershops() {
        assertTrue(barbershopRepository.getBarbershops().isEmpty());

        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);

        List<Barbershop> barbershop = barbershopRepository.getBarbershops();

        assertEquals(BARBERSHOP_VALID_ENTITY_LIST.size(), barbershop.size());
        assertEquals(BARBERSHOP_VALID_ENTITY_LIST, barbershop);
    }

    @Test
    void barbershopExistsById() {
        assertFalse(barbershopRepository.barbershopExistsById(BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID));

        Long id = barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);

        assertTrue(barbershopRepository.barbershopExistsById(id));
    }

    @Test
    void deactivateBarbershop() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);

        barbershopRepository.deactivateBarbershop(BARBERSHOP_VALID_BARBERSHOP_ID);

        assertTrue(barbershopRepository.findBarbershopById(BARBERSHOP_VALID_BARBERSHOP_ID).isEmpty());
    }

    @Test
    void truncateAndRestartSequence() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.truncateAndRestartSequence();

        assertTrue(barbershopRepository.getBarbershops().isEmpty());

        Long id = barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);

        assertEquals(1, id);
    }

    @AfterEach
    void cleanUpDb() {
        barbershopRepository.truncateAndRestartSequence();
    }
}
