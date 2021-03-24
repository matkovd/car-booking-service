package com.github.matkovd.carbookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedBookingResponseDto {
    private Long id;
    private Long carId;
    private Long startTime;
    private Long endTime;
    private ContactDetailsDto contactDetailsDto;
}
