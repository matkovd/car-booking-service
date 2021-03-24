package com.github.matkovd.carbookingservice.service;

import com.github.matkovd.carbookingservice.dto.BookingRequestDto;
import com.github.matkovd.carbookingservice.dto.BookingResponseDto;
import com.github.matkovd.carbookingservice.dto.DetailedBookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto bookingRequestDto);
    List<DetailedBookingResponseDto> getAll();
    List<DetailedBookingResponseDto> getByCarIdAndTimeBetween(Long carId, Long startTime, Long endTime);
    void delete(Long id);
}
