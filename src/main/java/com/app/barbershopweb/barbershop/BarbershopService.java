package com.app.barbershopweb.barbershop;

import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarbershopService {

    private final BarbershopRepository barbershopRepository;

    public BarbershopService(BarbershopRepository barbershopRepository) {
        this.barbershopRepository = barbershopRepository;
    }

    public Long addBarbershop(Barbershop barbershop) {
        return barbershopRepository.addBarbershop(barbershop);
    }

    public void deleteBarbershopById(Long id) {
        barbershopRepository.deleteBarbershopById(id);
    }

    public Optional<Barbershop> updateBarbershop(Barbershop barbershop) {
        return barbershopRepository.updateBarbershop(barbershop);
    }

    public Optional<Barbershop> findBarbershopById(Long id) {
        return barbershopRepository.findBarbershopById(id);
    }

    public List<Barbershop> getBarbershops() {
        return barbershopRepository.getBarbershops();
    }
}
