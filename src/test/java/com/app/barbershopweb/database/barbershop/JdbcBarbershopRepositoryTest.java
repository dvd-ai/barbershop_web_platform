package com.app.barbershopweb.database.barbershop;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcBarbershopRepositoryTest extends AbstractJdbcRepositoryTest {

    static JdbcBarbershopRepository barbershopRepository;
    BarbershopTestConstants btc = new BarbershopTestConstants();
    
    @BeforeAll
    static void init() {
        barbershopRepository = new JdbcBarbershopRepository(getDataSource());
    }

    @Test
    void addBarbershop() {
        Long id = barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        assertTrue(id > 0);
    }

    @Test
    void findBarbershopByExistedId() {
        Long id = barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        Optional<Barbershop> barbershopOptional = barbershopRepository.findBarbershopById(id);

        assertTrue(barbershopOptional.isPresent());
        assertEquals(btc.VALID_BARBERSHOP_ENTITY, barbershopOptional.get());
    }

    @Test
    void findBarbershopByNotExistingId() {
        Optional<Barbershop> barbershopOptional = barbershopRepository.findBarbershopById(
                btc.NOT_EXISTED_BARBERSHOP_ID
        );

        assertTrue(barbershopOptional.isEmpty());
    }

    @Test
    void updateExistingBarbershop() {
        barbershopRepository.addBarbershop(
                btc.VALID_BARBERSHOP_ENTITY
        );

        Optional<Barbershop> updBarbershopOptional = barbershopRepository.updateBarbershop(
                btc.VALID_UPDATED_BARBERSHOP_ENTITY
        );

        assertTrue(updBarbershopOptional.isPresent());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_ENTITY, updBarbershopOptional.get());
    }

    @Test
    void updateNotExistingBarbershop() {
        Optional<Barbershop> updBarbershopOptional = barbershopRepository.updateBarbershop(
                btc.VALID_UPDATED_BARBERSHOP_ENTITY
        );

        assertTrue(updBarbershopOptional.isEmpty());
    }

    @Test
    void getBarbershops() {
        assertTrue(barbershopRepository.getBarbershops().isEmpty());

        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);

        List<Barbershop> barbershop = barbershopRepository.getBarbershops();

        assertEquals(btc.VALID_BARBERSHOP_ENTITY_LIST.size(), barbershop.size());
        assertEquals(btc.VALID_BARBERSHOP_ENTITY_LIST, barbershop);
    }

    @Test
    void deleteBarbershopById() {
        Long id = barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.deleteBarbershopById(id);

        assertTrue(barbershopRepository.getBarbershops().isEmpty());
    }

    @Test
    void barbershopExistsById() {
        assertFalse(barbershopRepository.barbershopExistsById(btc.NOT_EXISTED_BARBERSHOP_ID));

        Long id = barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);

        assertTrue(barbershopRepository.barbershopExistsById(id));
    }

    @Test
    void truncateAndRestartSequence() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.truncateAndRestartSequence();

        assertTrue(barbershopRepository.getBarbershops().isEmpty());

        Long id = barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);

        assertEquals(1, id);
    }

    @AfterEach
    void cleanUpDb() {
        barbershopRepository.truncateAndRestartSequence();
    }
}
