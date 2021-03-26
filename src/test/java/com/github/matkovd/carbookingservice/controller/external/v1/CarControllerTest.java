package com.github.matkovd.carbookingservice.controller.external.v1;

import com.github.matkovd.carbookingservice.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
    @Mock
    private CarService carService;

    @Test
    void getAll_Returns200() {
        when(carService.getAll()).thenReturn(new ArrayList<>());
        var controller = new CarController(carService);
        assertEquals(controller.getAll().getStatusCode(), HttpStatus.OK);
    }
}
