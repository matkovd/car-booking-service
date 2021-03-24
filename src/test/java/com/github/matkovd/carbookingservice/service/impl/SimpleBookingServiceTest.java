package com.github.matkovd.carbookingservice.service.impl;

import com.github.matkovd.carbookingservice.config.BookingConfig;
import com.github.matkovd.carbookingservice.dao.BookingDao;
import com.github.matkovd.carbookingservice.dao.CarDao;
import com.github.matkovd.carbookingservice.dao.ContactDetailsDao;
import com.github.matkovd.carbookingservice.dto.*;
import com.github.matkovd.carbookingservice.exception.CarIsNotAvailableException;
import com.github.matkovd.carbookingservice.exception.CarNotFoundException;
import com.github.matkovd.carbookingservice.model.Booking;
import com.github.matkovd.carbookingservice.model.Car;
import com.github.matkovd.carbookingservice.model.ContactDetails;
import com.github.matkovd.carbookingservice.model.DetailedBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleBookingServiceTest {
    private static final Duration BOOKING_DURATION = Duration.of(30, ChronoUnit.MILLIS);
    private static final long END_TIME = BOOKING_DURATION.toMillis();
    private static final Long CAR_ID = 1L;
    private static final Long START_TIME = 0L;
    private static final Long CONTACT_DETAILS_ID = 1L;
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String ID_NUMBER = "123";
    private static final String PHONE = "777711122233";

    @Mock
    private CarDao carDao;
    @Mock
    private BookingDao bookingDao;
    @Mock
    private ContactDetailsDao contactDetailsDao;
    @Mock
    private BookingConfig bookingConfig;

    private Clock clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);


    @Test
    void create_CarIsAvailableContactDetailsNotPresent_Success() {
        var bookingRequest = new BookingRequestDto(CAR_ID, NAME, SURNAME, ID_NUMBER, PHONE);
        var contactDetails = new ContactDetails(0L, NAME, SURNAME, ID_NUMBER, PHONE);
        var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
        when(carDao.getById(eq(bookingRequest.getCarId()))).thenReturn(new Car(CAR_ID, "123ABC", "test"));
        when(bookingDao.getByCarIdAndTimeBetween(eq(bookingRequest.getCarId()), anyLong(), anyLong()))
                .thenReturn(new ArrayList<>());
        when(contactDetailsDao.getExactMatchId(eq(contactDetails)))
                .thenReturn(null);
        when(contactDetailsDao.create(eq(contactDetails)))
                .thenReturn(CONTACT_DETAILS_ID);
        when(bookingConfig.getPeriod()).thenReturn(BOOKING_DURATION);
        var bookingEntity = new Booking(
                0L,
                CAR_ID,
                CONTACT_DETAILS_ID,
                START_TIME,
                END_TIME,
                false
        );
        when(bookingDao.create(eq(bookingEntity))).thenReturn(1L);

        var expected = new BookingResponseDto(1L, CAR_ID, START_TIME, END_TIME);
        var result = bookingService.create(bookingRequest);
        assertEquals(expected, result);
    }

    @Test
    void create_CarIsAvailableContactDetailsPresent_DoesntCreateNewContactDetails() {
        var bookingRequest = new BookingRequestDto(CAR_ID, NAME, SURNAME, ID_NUMBER, PHONE);
        var contactDetails = new ContactDetails(0L, NAME, SURNAME, ID_NUMBER, PHONE);
        var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
        when(carDao.getById(eq(bookingRequest.getCarId()))).thenReturn(new Car(CAR_ID, "123ABC", "test"));
        when(bookingDao.getByCarIdAndTimeBetween(eq(bookingRequest.getCarId()), anyLong(), anyLong()))
                .thenReturn(new ArrayList<>());
        when(contactDetailsDao.getExactMatchId(eq(contactDetails)))
                .thenReturn(CONTACT_DETAILS_ID);

        when(bookingConfig.getPeriod()).thenReturn(BOOKING_DURATION);
        var bookingEntity = new Booking(
                0L,
                CAR_ID,
                CONTACT_DETAILS_ID,
                START_TIME,
                END_TIME,
                false
        );
        when(bookingDao.create(eq(bookingEntity))).thenReturn(1L);

        var expected = new BookingResponseDto(1L, CAR_ID, START_TIME, END_TIME);
        var result = bookingService.create(bookingRequest);
        assertEquals(expected, result);
        verify(contactDetailsDao, times(0)).create(eq(contactDetails));
    }

    @Test
    void create_CarIsNotAvailable_ThrowsCarIsNotAvailableException() {
        assertThrows(CarIsNotAvailableException.class, () -> {
            var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
            var bookingRequest = new BookingRequestDto(CAR_ID, NAME, SURNAME, ID_NUMBER, PHONE);
            when(carDao.getById(eq(bookingRequest.getCarId()))).thenReturn(new Car(CAR_ID, "123ABC", "test"));
            when(bookingDao.getByCarIdAndTimeBetween(eq(bookingRequest.getCarId()), anyLong(), anyLong()))
                    .thenReturn(List.of(new DetailedBooking()));
            bookingService.create(bookingRequest);
        });
    }

    @Test
    void create_CarIsNotNot_ThrowsCarNotFoundException() {
        assertThrows(CarNotFoundException.class, () -> {
            var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
            var bookingRequest = new BookingRequestDto(CAR_ID, NAME, SURNAME, ID_NUMBER, PHONE);
            when(carDao.getById(eq(bookingRequest.getCarId()))).thenThrow(new CarNotFoundException());
            bookingService.create(bookingRequest);
        });
    }

    @Test
    void getAll_EmptyListFromDao_ReturnsEmptyList() {
        when(bookingDao.getAll()).thenReturn(new ArrayList<>());
        var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
        assertEquals(new ArrayList<>(), bookingService.getAll());
    }

    @Test
    void getAll_MapsEntitiesToDto() {
        when(bookingDao.getAll()).thenReturn(List.of(
                new DetailedBooking(
                        1L,
                        CAR_ID,
                        START_TIME,
                        END_TIME,
                        new ContactDetails(
                                CONTACT_DETAILS_ID,
                                NAME,
                                SURNAME,
                                ID_NUMBER,
                                PHONE
                        )
                )
        ));

        var expected = List.of(
                new DetailedBookingResponseDto(
                        1L,
                        CAR_ID,
                        START_TIME,
                        END_TIME,
                        new ContactDetailsDto(
                                NAME,
                                SURNAME,
                                ID_NUMBER,
                                PHONE
                        )
                )

        );

        var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
        assertEquals(expected, bookingService.getAll());
    }

    @Test
    void getByCarIdAndTimeBetween_EmptyListFromDao_ReturnsEmptyList() {
        when(bookingDao.getByCarIdAndTimeBetween(eq(CAR_ID), eq(START_TIME), eq(END_TIME)))
                .thenReturn(new ArrayList<>());
        var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
        assertEquals(new ArrayList<>(), bookingService.getByCarIdAndTimeBetween(CAR_ID, START_TIME, END_TIME));
    }

    @Test
    void getByCarIdAndTimeBetween_MapsEntitiesToDto() {
        when(bookingDao.getByCarIdAndTimeBetween(eq(CAR_ID), eq(START_TIME), eq(END_TIME)))
                .thenReturn(List.of(
                new DetailedBooking(
                        1L,
                        CAR_ID,
                        START_TIME,
                        END_TIME,
                        new ContactDetails(
                                CONTACT_DETAILS_ID,
                                NAME,
                                SURNAME,
                                ID_NUMBER,
                                PHONE
                        )
                )
        ));

        var expected = List.of(
                new DetailedBookingResponseDto(
                        1L,
                        CAR_ID,
                        START_TIME,
                        END_TIME,
                        new ContactDetailsDto(
                                NAME,
                                SURNAME,
                                ID_NUMBER,
                                PHONE
                        )
                )

        );

        var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
        assertEquals(expected, bookingService.getByCarIdAndTimeBetween(CAR_ID, START_TIME, END_TIME));
    }

    @Test
    void delete_CallsDao_Success() {
        var bookingService = new SimpleBookingService(bookingDao, contactDetailsDao, carDao, bookingConfig, clock);
        bookingService.delete(1L);
        verify(bookingDao, times(1)).delete(eq(1L));
    }
}