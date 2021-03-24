package com.github.matkovd.carbookingservice.service.impl;

import com.github.matkovd.carbookingservice.dao.CarDao;
import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import com.github.matkovd.carbookingservice.dto.CarResponseDto;
import com.github.matkovd.carbookingservice.model.Car;
import com.github.matkovd.carbookingservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimpleCarService implements CarService {
    private final CarDao carDao;

    @Autowired
    public SimpleCarService(CarDao carDao) {
        this.carDao = carDao;
    }


    @Override
    public CarResponseDto create(CarRequestDto carRequestDto) {
        var carId = carDao.create(new Car(
                0L,
                carRequestDto.getNumber(),
                carRequestDto.getDescription()
        ));
        var car = carDao.getById(carId);
        return new CarResponseDto(
                car.getId(),
                car.getNumber(),
                car.getDescription()
        );
    }

    @Override
    public List<CarResponseDto> getAll() {
        return carDao.getAll()
                .stream()
                .map(it -> new CarResponseDto(it.getId(), it.getNumber(), it.getDescription()))
                .collect(Collectors.toList());
    }
}
