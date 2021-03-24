package com.github.matkovd.carbookingservice.dao.impl;

import com.github.matkovd.carbookingservice.model.Booking;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
class MySQLBookingDaoIntegrationTest extends AbstractDaoIntegrationTest {

    private static MySQLBookingDao bookingDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() {
        jdbcTemplate = jdbcTemplate();
        loadSqlFile(jdbcTemplate, "schema.sql");
        loadSqlFile(jdbcTemplate, "data.sql");
        bookingDao = new MySQLBookingDao(jdbcTemplate);
    }

    @Test
    void create_CarIdAndContactDetailsPresent_CreatesSuccessfully() {
        var booking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 0L, 10L, false);
        var id = bookingDao.create(booking);
        var result = bookingDao.getAll().stream().filter(it -> it.getId().equals(id)).findFirst();
        assertTrue(result.isPresent());
        var bookingEntity = result.get();
        assertEquals(booking.getCarId(), bookingEntity.getCarId());
        assertEquals(booking.getContactDetailsId(), bookingEntity.getContactDetails().getId());
        assertEquals(booking.getStartTime(), bookingEntity.getStartTime());
        assertEquals(booking.getEndTime(), bookingEntity.getEndTime());
        bookingDao.delete(id);
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void create_CarIdAndContactDetailsNotPresent_ThrowsDataIntegrityException() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            var booking = new Booking(0L, 99L, 123L, 0L, 10L, false);
            bookingDao.create(booking);
        });
    }

    @Test
    void getAll_DeletedReferencesInDb_ReturnsOmitsDeletedSuccessfully() {
        var booking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 0L, 10L, false);
        var bookingToDelete = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 12L, 22L, false);
        var bookingId = bookingDao.create(booking);
        var bookingIdToDelete = bookingDao.create(bookingToDelete);
        bookingDao.delete(bookingIdToDelete);
        var bookings = bookingDao.getAll();
        assertEquals(1, bookings.size());
        assertEquals(bookingId, bookings.get(0).getId());
        bookingDao.delete(bookingId);
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void getAll_EmptyDatabase_ReturnsEmptyList() {
        bookingDao.getAll().forEach(it -> bookingDao.delete(it.getId()));
        var bookings = bookingDao.getAll();
        assertEquals(0, bookings.size());
        putDatabaseToInitialState(jdbcTemplate);
    }


    @Test
    void delete_ExistingEntity_DeletesSuccessfully() {
        var booking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 0L, 10L, false);
        var id = bookingDao.create(booking);
        var insertedBooking = bookingDao.getAll().stream().filter(it -> it.getId().equals(id)).findFirst();
        assertTrue(insertedBooking.isPresent());
        bookingDao.delete(id);
        var deletedBooking = bookingDao.getAll().stream().filter(it -> it.getId().equals(id)).findFirst();
        assertFalse(deletedBooking.isPresent());
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void getByCarIdAndTimeBetween_CarIdAndBothStartEndTimeMatches_returnsAllEntities() {
        var firstBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 0L, 10L, false);
        var secondBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 12L, 22L, false);
        bookingDao.create(firstBooking);
        bookingDao.create(secondBooking);
        var result = bookingDao.getByCarIdAndTimeBetween(CAR_ID, 0L, 22L);
        assertEquals(2, result.size());
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void getByCarIdAndTimeBetween_CarIdDoesntMatchAndBothStartEndTimeMatches_returnsEmptyList() {
        var firstBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 0L, 10L, false);
        var secondBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 12L, 22L, false);
        bookingDao.create(firstBooking);
        bookingDao.create(secondBooking);
        var result = bookingDao.getByCarIdAndTimeBetween(99L, 0L, 22L);
        assertEquals(0, result.size());
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void getByCarIdAndTimeBetween_CarIdMatchAndOnlyStartTimeMatches_returnsMatchingEntity() {
        var firstBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 0L, 10L, false);
        var secondBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 12L, 22L, false);
        var firstId = bookingDao.create(firstBooking);
        bookingDao.create(secondBooking);
        var result = bookingDao.getByCarIdAndTimeBetween(CAR_ID, 0L, 9L);
        assertEquals(1, result.size());
        assertEquals(firstId, result.get(0).getId());
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void getByCarIdAndTimeBetween_CarIdMatchAndOnlyEndTimeMatches_returnsMatchingEntity() {
        var firstBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 0L, 10L, false);
        var secondBooking = new Booking(0L, CAR_ID, CONTACT_DETAILS_ID, 12L, 22L, false);
        bookingDao.create(firstBooking);
        var secondId = bookingDao.create(secondBooking);
        var result = bookingDao.getByCarIdAndTimeBetween(CAR_ID, 16L, 19L);
        assertEquals(1, result.size());
        assertEquals(secondId, result.get(0).getId());
        putDatabaseToInitialState(jdbcTemplate);
    }
}
