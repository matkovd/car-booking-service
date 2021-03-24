package com.github.matkovd.carbookingservice.service.impl;

import com.github.matkovd.carbookingservice.config.BookingConfig;
import com.github.matkovd.carbookingservice.dao.BookingDao;
import com.github.matkovd.carbookingservice.dao.CarDao;
import com.github.matkovd.carbookingservice.dao.ContactDetailsDao;
import com.github.matkovd.carbookingservice.dto.BookingRequestDto;
import com.github.matkovd.carbookingservice.dto.BookingResponseDto;
import com.github.matkovd.carbookingservice.dto.ContactDetailsDto;
import com.github.matkovd.carbookingservice.dto.DetailedBookingResponseDto;
import com.github.matkovd.carbookingservice.exception.CarIsNotAvailableException;
import com.github.matkovd.carbookingservice.model.Booking;
import com.github.matkovd.carbookingservice.model.Car;
import com.github.matkovd.carbookingservice.model.ContactDetails;
import com.github.matkovd.carbookingservice.model.DetailedBooking;
import com.github.matkovd.carbookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimpleBookingService implements BookingService {
    private final BookingDao bookingDao;
    private final ContactDetailsDao contactDetailsDao;
    private final CarDao carDao;
    private final BookingConfig bookingConfig;
    private final Clock clock;

    @Autowired
    public SimpleBookingService(BookingDao bookingDao,
                                ContactDetailsDao contactDetailsDao,
                                CarDao carDao,
                                BookingConfig bookingConfig,
                                Clock clock) {
        this.bookingDao = bookingDao;
        this.contactDetailsDao = contactDetailsDao;
        this.carDao = carDao;
        this.bookingConfig = bookingConfig;
        this.clock = clock;
    }

    @Override
    public BookingResponseDto create(BookingRequestDto bookingRequestDto) {
        var car = carDao.getById(bookingRequestDto.getCarId());
        var startTime = clock.millis();
        var endTime = startTime + bookingConfig.getPeriod().toMillis();
        if (!checkCarIsAvailableForPeriod(car, startTime, endTime)) {
            throw new CarIsNotAvailableException();
        }
        var contactDetails = new ContactDetails(
                0L,
                bookingRequestDto.getName(),
                bookingRequestDto.getSurname(),
                bookingRequestDto.getIdNumber(),
                bookingRequestDto.getPhone()
        );
        var contactDetailsId = contactDetailsDao.getExactMatchId(contactDetails);
        if (contactDetailsId == null) {
            contactDetailsId = contactDetailsDao.create(contactDetails);
        }
        var bookingId = bookingDao.create(new Booking(
                0L,
                car.getId(),
                contactDetailsId,
                startTime,
                endTime,
                false
        ));
        return new BookingResponseDto(bookingId, car.getId(), startTime, endTime);
    }

    @Override
    public List<DetailedBookingResponseDto> getAll() {
        return bookingDao.getAll().stream()
                .map(this::mapDetailedBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailedBookingResponseDto> getByCarIdAndTimeBetween(Long carId, Long startTime, Long endTime) {
        return bookingDao.getByCarIdAndTimeBetween(carId, startTime, endTime).stream()
                .map(this::mapDetailedBooking)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        bookingDao.delete(id);
    }

    private boolean checkCarIsAvailableForPeriod(Car car, Long startTime, Long endTime) {
        return bookingDao.getByCarIdAndTimeBetween(car.getId(), startTime, endTime).isEmpty();
    }

    private DetailedBookingResponseDto mapDetailedBooking(DetailedBooking detailedBooking) {
        return new DetailedBookingResponseDto(
                detailedBooking.getId(),
                detailedBooking.getCarId(),
                detailedBooking.getStartTime(),
                detailedBooking.getEndTime(),
                mapContactDetails(detailedBooking.getContactDetails())
        );
    }


    private ContactDetailsDto mapContactDetails(ContactDetails contactDetails) {
        return new ContactDetailsDto(
                contactDetails.getName(),
                contactDetails.getSurname(),
                contactDetails.getIdNumber(),
                contactDetails.getPhone()
        );
    }
}
