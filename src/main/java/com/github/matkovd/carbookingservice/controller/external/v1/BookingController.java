package com.github.matkovd.carbookingservice.controller.external.v1;

import com.github.matkovd.carbookingservice.dto.BookingRequestDto;
import com.github.matkovd.carbookingservice.dto.BookingResponseDto;
import com.github.matkovd.carbookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(BookingController.PATH)
public class BookingController {
    public static final String PATH = "/api/v1/bookings";

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto) {
        return new ResponseEntity<>(bookingService.create(bookingRequestDto), HttpStatus.CREATED);
    }

}
