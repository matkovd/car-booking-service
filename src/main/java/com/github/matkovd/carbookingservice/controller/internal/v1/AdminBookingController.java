package com.github.matkovd.carbookingservice.controller.internal.v1;

import com.github.matkovd.carbookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AdminBookingController.PATH)
public class AdminBookingController {
    public static final String PATH = "/internal/api/v1/bookings";

    private final BookingService bookingService;

    @Autowired
    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity getByCarIdAndTimeBetween(
            @RequestParam("carId") Long carId,
            @RequestParam("startTime") Long startTime,
            @RequestParam("endTime") Long endTime) {
        if (endTime <= startTime) {
            return ResponseEntity.badRequest().body("Invalid timeRange");
        }
        return ResponseEntity.ok(bookingService.getByCarIdAndTimeBetween(carId, startTime, endTime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
