package com.github.matkovd.carbookingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matkovd.carbookingservice.controller.external.v1.BookingController;
import com.github.matkovd.carbookingservice.controller.internal.v1.AdminCarController;
import com.github.matkovd.carbookingservice.dto.BookingRequestDto;
import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.github.matkovd.carbookingservice.constants.Constants.MYSQL_IMAGE;

public abstract class AbstractControllerIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("car_booking")
            .withUsername("car_booking")
            .withPassword("password");

    protected MockHttpServletResponse createCar(CarRequestDto carRequestDto) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(AdminCarController.PATH)
                        .content(asJsonString(carRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }

    protected MockHttpServletResponse createBooking(BookingRequestDto bookingRequestDto) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(BookingController.PATH)
                        .content(asJsonString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }

    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
