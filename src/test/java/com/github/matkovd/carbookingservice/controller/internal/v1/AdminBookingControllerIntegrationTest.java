package com.github.matkovd.carbookingservice.controller.internal.v1;

import com.github.matkovd.carbookingservice.controller.AbstractControllerIntegrationTest;
import com.github.matkovd.carbookingservice.dto.BookingRequestDto;
import com.github.matkovd.carbookingservice.dto.BookingResponseDto;
import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import com.github.matkovd.carbookingservice.dto.CarResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:tc:mysql:8:///car_booking",
                "spring.datasource.username=car_booking",
                "spring.datasource.password=car_booking"
        }
)
class AdminBookingControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    void getByCarIdAndTimeBetween_MatchesTime_Returns200AndBooking() throws Exception {
        var expectedCar = new CarRequestDto("getByCarIdAndTimeBetween", "description");
        var response = createCar(expectedCar);
        var carDto = new ObjectMapper().readValue(response.getContentAsString(), CarResponseDto.class);
        var bookingResponse = createBooking(new BookingRequestDto(
                carDto.getId(),
                "getByCarIdAndTimeBetweenTestName",
                "surname",
                "idNumber",
                "phone"
        ));
        var booking = new ObjectMapper().readValue(bookingResponse.getContentAsString(), BookingResponseDto.class);

        var result = this.mockMvc.perform(
                MockMvcRequestBuilders.get(AdminBookingController.PATH)
                        .param("carId", booking.getCarId().toString())
                        .param("startTime", "0")
                        .param("endTime", booking.getEndTime().toString())

        ).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains("getByCarIdAndTimeBetweenTestName"));
    }

    @Test
    void getByCarIdAndTimeBetween_DoesntMatchTime_Returns200AndDoesntReturnBooking() throws Exception {
        var expectedCar = new CarRequestDto("getByCarIdAndTimeBetween", "description");
        var response = createCar(expectedCar);
        var carDto = new ObjectMapper().readValue(response.getContentAsString(), CarResponseDto.class);
        var bookingResponse = createBooking(new BookingRequestDto(
                carDto.getId(),
                "getByCarIdAndTimeBetweenTestName",
                "surname",
                "idNumber",
                "phone"
        ));
        var booking = new ObjectMapper().readValue(bookingResponse.getContentAsString(), BookingResponseDto.class);

        var result = this.mockMvc.perform(
                MockMvcRequestBuilders.get(AdminBookingController.PATH)
                        .param("carId", booking.getCarId().toString())
                        .param("startTime", "0")
                        .param("endTime", "1")

        ).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertFalse(result.getResponse().getContentAsString().contains("getByCarIdAndTimeBetweenTestName"));
    }

    @Test
    void delete_DeletesEntity_Returns204() throws Exception {
        var expectedCar = new CarRequestDto("getByCarIdAndTimeBetween", "description");
        var response = createCar(expectedCar);
        var carDto = new ObjectMapper().readValue(response.getContentAsString(), CarResponseDto.class);
        var bookingResponse = createBooking(new BookingRequestDto(
                carDto.getId(),
                "entityToBeDeleted",
                "surname",
                "idNumber",
                "phone"
        ));
        var booking = new ObjectMapper().readValue(bookingResponse.getContentAsString(), BookingResponseDto.class);

        var bookings = this.mockMvc.perform(
                MockMvcRequestBuilders.get(AdminBookingController.PATH)
                        .param("carId", booking.getCarId().toString())
                        .param("startTime", "0")
                        .param("endTime", booking.getEndTime().toString())

        ).andReturn();
        assertEquals(bookings.getResponse().getStatus(), HttpStatus.OK.value());
        assertTrue(bookings.getResponse().getContentAsString().contains("entityToBeDeleted"));

        var result = this.mockMvc.perform(
                MockMvcRequestBuilders.delete(AdminBookingController.PATH + "/" + booking.getId().toString())
        ).andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());

        var bookingsAfterDeletion = this.mockMvc.perform(
                MockMvcRequestBuilders.get(AdminBookingController.PATH)
                        .param("carId", booking.getCarId().toString())
                        .param("startTime", "0")
                        .param("endTime", booking.getEndTime().toString())

        ).andReturn();
        assertEquals(HttpStatus.OK.value(), bookingsAfterDeletion.getResponse().getStatus());
        assertFalse(bookingsAfterDeletion.getResponse().getContentAsString().contains("entityToBeDeleted"));
    }

}
