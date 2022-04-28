package com.app.barbershopweb.barbershop.closure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.Min;

@Controller
@RequestMapping("/barbershops")
@Validated
public class BarbershopClosureController {

    private final BarbershopClosureService barbershopClosureService;

    public BarbershopClosureController(BarbershopClosureService barbershopClosureService) {
        this.barbershopClosureService = barbershopClosureService;
    }

    @DeleteMapping("/{barbershopId}")
    public ResponseEntity<Object> deleteBarbershop(@PathVariable @Min(1) Long barbershopId) {
        barbershopClosureService.outOfBusiness(barbershopId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
