package com.github.matkovd.carbookingservice.controller.external.v1;

import com.github.matkovd.carbookingservice.controller.AbstractControllerIntegrationTest;
import com.github.matkovd.carbookingservice.dto.CarRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:tc:mysql:8:///car_booking",
                "spring.datasource.username=car_booking",
                "spring.datasource.password=car_booking"
        }
)
class CarControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    void getAll_ReturnsValidData() throws Exception {
        var expectedCar = new CarRequestDto("getAll_ReturnsValidData", "description");
        createCar(expectedCar);
        var result = this.mockMvc.perform(
                MockMvcRequestBuilders.get(CarController.PATH)
        ).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(expectedCar.getNumber()));
        assertTrue(result.getResponse().getContentAsString().contains(expectedCar.getDescription()));
    }
}
