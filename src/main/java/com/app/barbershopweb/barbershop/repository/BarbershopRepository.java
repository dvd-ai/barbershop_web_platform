package com.app.barbershopweb.barbershop.repository;

import com.app.barbershopweb.barbershop.Barbershop;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarbershopRepository{

    Long addBarbershop(Barbershop barbershop);
    Optional<Barbershop> findBarbershopById(Long id);
    Optional<Barbershop>updateBarbershop(Barbershop barbershop);
    List<Barbershop> getBarbershops();
    void deleteBarbershopById(Long id);
    boolean barbershopExistsById(Long id);
    void truncateAndRestartSequence();
}
