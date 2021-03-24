package com.github.matkovd.carbookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    @NotNull
    private Long carId;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String idNumber;
    @NotBlank
    @Size(min = 5, max = 20)
    private String phone;
}
