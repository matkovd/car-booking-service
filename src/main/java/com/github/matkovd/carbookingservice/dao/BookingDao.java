package com.github.matkovd.carbookingservice.dao;

import com.github.matkovd.carbookingservice.model.Booking;
import com.github.matkovd.carbookingservice.model.DetailedBooking;

import java.util.List;

public interface BookingDao {
    Long create(Booking booking);
    void delete(Long id);
    List<DetailedBooking> getAll();
    List<DetailedBooking> getByCarIdAndTimeBetween(Long carId, Long startTime, Long endTime);
}
