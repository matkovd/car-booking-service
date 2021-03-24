package com.github.matkovd.carbookingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CarIsNotAvailableException extends RuntimeException {
    public CarIsNotAvailableException() {
        super("Car is not available");
    }
}
