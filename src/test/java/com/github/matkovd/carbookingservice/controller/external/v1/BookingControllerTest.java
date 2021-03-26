package com.github.matkovd.carbookingservice.controller.external.v1;

import com.github.matkovd.carbookingservice.dto.BookingRequestDto;
import com.github.matkovd.carbookingservice.dto.BookingResponseDto;
import com.github.matkovd.carbookingservice.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @Test
    void create_Returns201() {
        var request = new BookingRequestDto(1L, "name", "surname", "123", "123123");
        when(bookingService.create(eq(request))).thenReturn(new BookingResponseDto());
        var controller = new BookingController(bookingService);
        var result = controller.createBooking(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }
}
