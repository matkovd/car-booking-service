package com.github.matkovd.carbookingservice.controller.external.v1;

import com.github.matkovd.carbookingservice.controller.AbstractControllerIntegrationTest;
import com.github.matkovd.carbookingservice.dto.BookingRequestDto;
import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import com.github.matkovd.carbookingservice.dto.CarResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:tc:mysql:8:///car_booking",
                "spring.datasource.username=car_booking",
                "spring.datasource.password=car_booking"
        }
)
class BookingControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    void create_ValidRequest_Returns201() throws Exception {
        var expectedCar = new CarRequestDto("createCar", "description");
        var response = createCar(expectedCar);
        var carDto = new ObjectMapper().readValue(response.getContentAsString(), CarResponseDto.class);
        var result = createBooking(new BookingRequestDto(
                carDto.getId(),
                "name",
                "surname",
                "idNumber",
                "phone"
        ));
        assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    }

    @Test
    void create_NonExistingCar_Returns404() throws Exception {
        var result = createBooking(new BookingRequestDto(
            99L,"name", "surname", "12355", "2828327"
        ));
        assertEquals(result.getStatus(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_InvalidRequest_Returns400() throws Exception {
        var result = createBooking(new BookingRequestDto(
                null,"", "", "", ""
        ));
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
    }

    @Test
    void create_AlreadyBooking_ReturnsConflict() throws Exception {
        var expectedCar = new CarRequestDto("createCar", "description");
        var response = createCar(expectedCar);
        var carDto = new ObjectMapper().readValue(response.getContentAsString(), CarResponseDto.class);
        createBooking(new BookingRequestDto(
                carDto.getId(),
                "name",
                "surname",
                "idNumber",
                "phone"
        ));
        var result = createBooking(new BookingRequestDto(
                carDto.getId(),
                "name",
                "surname",
                "idNumber",
                "phone"
        ));

        assertEquals(HttpStatus.CONFLICT.value(), result.getStatus());
    }
}
