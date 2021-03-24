package com.github.matkovd.carbookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDto {
    @NotBlank
    @Size(min = 3)
    private String number;
    @NotBlank
    private String description;
}
