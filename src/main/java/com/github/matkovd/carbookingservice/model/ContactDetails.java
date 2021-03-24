package com.github.matkovd.carbookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetails {
    private Long id;
    private String name;
    private String surname;
    private String idNumber;
    private String phone;
}
