package com.github.matkovd.carbookingservice.controller.external.v1;


import com.github.matkovd.carbookingservice.dto.CarResponseDto;
import com.github.matkovd.carbookingservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(CarController.PATH)
public class CarController {
    public static final String PATH = "/api/v1/cars";

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAll() {
        return ResponseEntity.ok(carService.getAll());
    }
}
