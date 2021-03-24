package com.github.matkovd.carbookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetailsDto {
    private String name;
    private String surname;
    private String idNumber;
    private String phone;
}
