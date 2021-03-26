package com.github.matkovd.carbookingservice.controller.internal.v1;

import com.github.matkovd.carbookingservice.controller.external.v1.CarController;
import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import com.github.matkovd.carbookingservice.dto.CarResponseDto;
import com.github.matkovd.carbookingservice.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCarControllerTest {
    @Mock
    private CarService carService;

    private static final String NUMBER = "123";
    private static final String DESCRIPTION = "description";

    @Test
    void getAll_Returns200() {
        when(carService.getAll()).thenReturn(new ArrayList<>());
        var controller = new AdminCarController(carService);
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
    }

    @Test
    void create_Returns201() {
        var request = new CarRequestDto(NUMBER, DESCRIPTION);
        when(carService.create(eq(request))).thenReturn(new CarResponseDto());
        var controller = new AdminCarController(carService);
        assertEquals(HttpStatus.CREATED, controller.create(request).getStatusCode());
    }
}