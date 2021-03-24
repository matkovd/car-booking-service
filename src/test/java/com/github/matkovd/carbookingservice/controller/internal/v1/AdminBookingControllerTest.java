package com.github.matkovd.carbookingservice.controller.internal.v1;

import com.github.matkovd.carbookingservice.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminBookingControllerTest {
    @Mock
    private BookingService bookingService;

    private static Long CAR_ID = 0L;
    private static Long START_TIME = 1L;
    private static Long END_TIME = 3L;
    private static Long BOOKING_ID = 4L;

    @Test
    void getByCarIdAndTimeBetween_ValidParams_Returns200() {
        when(bookingService.getByCarIdAndTimeBetween(eq(CAR_ID), eq(START_TIME), eq(END_TIME)))
                .thenReturn(new ArrayList<>());
        var controller = new AdminBookingController(bookingService);
        var result = controller.getByCarIdAndTimeBetween(CAR_ID, START_TIME, END_TIME);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getByCarIdAndTimeBetween_InvalidParams_Returns400() {
        var controller = new AdminBookingController(bookingService);
        var result = controller.getByCarIdAndTimeBetween(CAR_ID, END_TIME, START_TIME);
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteBooking_Returns204() {
        var controller = new AdminBookingController(bookingService);
        assertEquals(controller.deleteBooking(BOOKING_ID).getStatusCode(), HttpStatus.NO_CONTENT);
        verify(bookingService, times(1)).delete(eq(BOOKING_ID));
    }
}