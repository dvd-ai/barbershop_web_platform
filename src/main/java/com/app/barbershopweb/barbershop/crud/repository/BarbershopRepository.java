package com.app.barbershopweb.barbershop.crud.repository;

import com.app.barbershopweb.barbershop.crud.Barbershop;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarbershopRepository {

    Long addBarbershop(Barbershop barbershop);

    Optional<Barbershop> findBarbershopById(Long id);

    Optional<Barbershop> updateBarbershop(Barbershop barbershop);

    List<Barbershop> getBarbershops();

    boolean barbershopExistsById(Long id);

    void deactivateBarbershop(Long barbershopId);

    void truncateAndRestartSequence();
}
