package com.github.matkovd.carbookingservice.dao.impl;

import com.github.matkovd.carbookingservice.dao.BookingDao;
import com.github.matkovd.carbookingservice.model.Booking;
import com.github.matkovd.carbookingservice.model.ContactDetails;
import com.github.matkovd.carbookingservice.model.DetailedBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class MySQLBookingDao implements BookingDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<DetailedBooking> rowMapper;

    @Autowired
    public MySQLBookingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new DetailedBookingRowMapper();
    }

    @Override
    public Long create(Booking booking) {
        var sql = "INSERT INTO bookings (car_id, contact_details_id, start_time, end_time) values (?, ?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    final var preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setLong(1, booking.getCarId());
                    preparedStatement.setLong(2, booking.getContactDetailsId());
                    preparedStatement.setLong(3, booking.getStartTime());
                    preparedStatement.setLong(4, booking.getEndTime());
                    return preparedStatement;
                }, keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    @Override
    public void delete(Long id) {
        var sql = "UPDATE bookings SET deleted=true WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<DetailedBooking> getAll() {
        var sql = "SELECT bookings.id, bookings.car_id, bookings.start_time, bookings.end_time, " +
                "contact_details.id, contact_details.name, contact_details.surname, " +
                "contact_details.id_number, contact_details.phone " +
                "FROM bookings INNER JOIN contact_details ON bookings.contact_details_id=contact_details.id " +
                "WHERE bookings.deleted=false";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<DetailedBooking> getByCarIdAndTimeBetween(Long carId, Long startTime, Long endTime) {
        var sql = "SELECT bookings.id, bookings.car_id, bookings.start_time, bookings.end_time, " +
                "contact_details.id, contact_details.name, contact_details.surname," +
                "contact_details.id_number, contact_details.phone " +
                "FROM bookings INNER JOIN contact_details ON bookings.contact_details_id=contact_details.id " +
                "WHERE bookings.car_id = ? " +
                "AND (? BETWEEN bookings.start_time AND bookings.end_time " +
                "or ? BETWEEN bookings.start_time AND bookings.end_time) " +
                "AND deleted=false";
        return jdbcTemplate.query(sql, rowMapper, carId, startTime, endTime);
    }

    private class DetailedBookingRowMapper implements RowMapper<DetailedBooking> {
        @Override
        public DetailedBooking mapRow(ResultSet rs, int rowNum) throws SQLException {
            var detailedBooking = new DetailedBooking();
            var contactDetails = new ContactDetails();
            detailedBooking.setId(rs.getLong("bookings.id"));
            detailedBooking.setCarId(rs.getLong("bookings.car_id"));
            detailedBooking.setStartTime(rs.getLong("bookings.start_time"));
            detailedBooking.setEndTime(rs.getLong("bookings.end_time"));
            contactDetails.setId(rs.getLong("contact_details.id"));
            contactDetails.setName(rs.getString("contact_details.name"));
            contactDetails.setSurname(rs.getString("contact_details.surname"));
            contactDetails.setPhone(rs.getString("contact_details.phone"));
            contactDetails.setIdNumber(rs.getString("contact_details.id_number"));
            detailedBooking.setContactDetails(contactDetails);
            return detailedBooking;
        }
    }
}
