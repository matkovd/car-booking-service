package com.github.matkovd.carbookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedBooking {
    private Long id;
    private Long carId;
    private Long startTime;
    private Long endTime;
    private ContactDetails contactDetails;
}
