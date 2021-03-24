package com.github.matkovd.carbookingservice.controller.internal.v1;

import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import com.github.matkovd.carbookingservice.dto.CarResponseDto;
import com.github.matkovd.carbookingservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(AdminCarController.PATH)
public class AdminCarController {
    public static final String PATH = "/internal/api/v1/cars";

    private CarService carService;

    @Autowired
    public AdminCarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAll() {
        return ResponseEntity.ok(carService.getAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody CarRequestDto carRequestDto) {
        return new ResponseEntity<>(carService.create(carRequestDto), HttpStatus.CREATED);
    }
}
