package com.github.matkovd.carbookingservice.dao.impl;

import com.github.matkovd.carbookingservice.exception.CarNotFoundException;
import com.github.matkovd.carbookingservice.model.Car;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
class MySQLCarDaoIntegrationTest extends AbstractDaoIntegrationTest {
    private static MySQLCarDao carDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() {
        jdbcTemplate = jdbcTemplate();
        loadSqlFile(jdbcTemplate, "schema.sql");
        loadSqlFile(jdbcTemplate, "data.sql");
        carDao = new MySQLCarDao(jdbcTemplate);
    }

    @Test
    void create_ValidEntity_CreatesSuccessfully() {
        var car = new Car(0L, "123", "description");
        var id = carDao.create(car);
        var carFromDb = carDao.getById(id);
        assertEquals(car.getNumber(), carFromDb.getNumber());
        assertEquals(car.getDescription(), carFromDb.getDescription());
        putDatabaseToInitialState(jdbcTemplate);
    }


    @Test
    void getCarById_NonExistingId_ThrowsCarNotFoundException() {
        assertThrows(CarNotFoundException.class, () -> carDao.getById(99L));
    }

    @Test
    void getAll_ExistingRecord_Returns() {
        putDatabaseToInitialState(jdbcTemplate);
        var result = carDao.getAll();
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), CAR_ID);
    }

    @Test
    void getById_ExistingId_ReturnsCar() {
        var car = new Car(0L, "123", "description");
        var id = carDao.create(car);
        var carFromDb = carDao.getById(id);
        assertEquals(car.getNumber(), carFromDb.getNumber());
        assertEquals(car.getDescription(), carFromDb.getDescription());
        putDatabaseToInitialState(jdbcTemplate);
    }

}