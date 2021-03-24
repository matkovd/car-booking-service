package com.github.matkovd.carbookingservice.service;

import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import com.github.matkovd.carbookingservice.dto.CarResponseDto;

import java.util.List;

public interface CarService {
    CarResponseDto create(CarRequestDto carRequestDto);
    List<CarResponseDto> getAll();
}
