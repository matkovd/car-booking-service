package com.github.matkovd.carbookingservice.service.impl;

import com.github.matkovd.carbookingservice.dao.CarDao;
import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import com.github.matkovd.carbookingservice.dto.CarResponseDto;
import com.github.matkovd.carbookingservice.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleCarServiceTest {
    @Mock
    private CarDao carDao;

    @Test
    void getAll_EmptyListFromDao_ReturnsEmptyList() {
        when(carDao.getAll()).thenReturn(new ArrayList<>());
        var carService = new SimpleCarService(carDao);
        assertEquals(new ArrayList<>(), carService.getAll());
    }

    @Test
    void getAll_MultipleEntities_MapsEntitiesToDto() {
        when(carDao.getAll()).thenReturn(List.of(
                new Car(1L, "123ABC", "description"),
                new Car(2L, "456DEF", "another description")
        ));
        var expected = List.of(
                new CarResponseDto(1L, "123ABC", "description"),
                new CarResponseDto(2L, "456DEF", "another description")
        );

        var carService = new SimpleCarService(carDao);
        assertEquals(expected, carService.getAll());
    }

    @Test
    void create_CreatesEntityAndMapsItToDto() {
        var carRequestDto = new CarRequestDto(
                "123ABC",
                "description"
        );
        var carEntity = new Car(
                0L,
                "123ABC",
                "description"
        );
        var expectedCar = new Car(
                1L,
                "123ABC",
                "description"
        );
        var expectedCarDto = new CarResponseDto(
                expectedCar.getId(),
                expectedCar.getNumber(),
                expectedCar.getDescription()
        );

        when(carDao.create(eq(carEntity))).thenReturn(expectedCar.getId());
        when(carDao.getById(expectedCar.getId())).thenReturn(expectedCar);

        var carService = new SimpleCarService(carDao);
        assertEquals(expectedCarDto, carService.create(carRequestDto));
    }
}
