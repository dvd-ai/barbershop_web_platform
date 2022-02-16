package com.app.barbershopweb.barbershop;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BarbershopService {

    private final List<Barbershop> barbershopList = new ArrayList<>();

    public Long addBarbershop(Barbershop barbershop) {
        Long id = barbershopList.size() + 1L;
        barbershop.setId(id);
        this.barbershopList.add(barbershop);
        return id;
    }

    public void deleteBarbershopById(Long id) {
        barbershopList.removeIf(barbershop -> barbershop.getId().equals(id));
    }

    public Optional<Barbershop> updateBarbershop(Barbershop barbershop) {
        return barbershopList.stream()
                .filter(barbershop1 -> barbershop1.getId().equals(barbershop.getId()))
                .peek(barbershop1 -> {
                    barbershop1.setAddress(barbershop.getAddress());
                    barbershop1.setName(barbershop.getName());
                    barbershop1.setPhoneNumber(barbershop.getPhoneNumber());
                    barbershop1.setEmail(barbershop.getEmail());
                })
                .findFirst();
    }

    public Optional<Barbershop> findBarbershopById(Long id) {
        return barbershopList.stream()
                .filter(barbershop -> barbershop.getId().equals(id))
                .findFirst();
    }

    public List<Barbershop> getBarbershops() {
        return barbershopList;
    }
}
