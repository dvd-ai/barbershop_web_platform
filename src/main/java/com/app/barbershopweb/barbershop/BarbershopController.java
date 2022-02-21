package com.app.barbershopweb.barbershop;

import com.app.barbershopweb.barbershop.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/barbershops")
@Validated
public class BarbershopController {

    private final BarbershopService barbershopService;
    private final BarbershopConverter barbershopConverter;

    public BarbershopController(BarbershopService barbershopService, BarbershopConverter barbershopConverter) {
        this.barbershopService = barbershopService;
        this.barbershopConverter = barbershopConverter;
    }

    @GetMapping
    public ResponseEntity<List<BarbershopDto>> getBarbershops() {
        return new ResponseEntity<>(
          barbershopConverter.barbershopEntityListToDtoList(barbershopService.getBarbershops()), HttpStatus.OK
        );
    }

    @GetMapping("/{barbershopId}")
    public ResponseEntity<BarbershopDto> getBarbershopById(@PathVariable @Min(1) Long barbershopId) {
        Barbershop barbershop = barbershopService.findBarbershopById(barbershopId)
                .orElseThrow(() -> new NotFoundException("Barbershop with id " + barbershopId + " not found."));
        return new ResponseEntity<>(barbershopConverter.mapToDto(barbershop), HttpStatus.OK);
    }

    @PostMapping
    //id is obligation due to @Valid
    public ResponseEntity<Long> addBarbershop(@RequestBody @Valid BarbershopDto barbershopDto) {
        Barbershop entity = barbershopConverter.mapToEntity(barbershopDto);
        return new ResponseEntity<>(barbershopService.addBarbershop(entity), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Barbershop> updateBarbershop(@RequestBody @Valid BarbershopDto barbershopDto) {
        Barbershop entity = barbershopService.updateBarbershop(barbershopConverter.mapToEntity(barbershopDto))
                .orElseThrow(() -> new NotFoundException("Barbershop with id '" + barbershopDto.id() + "' not found."));
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @DeleteMapping("/{barbershopId}")
    public ResponseEntity<Object> deleteBarbershopById(@PathVariable @Min(1) Long barbershopId) {
        barbershopService.deleteBarbershopById(barbershopId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
