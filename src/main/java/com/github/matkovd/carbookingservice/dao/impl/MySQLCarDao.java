package com.github.matkovd.carbookingservice.dao.impl;

import com.github.matkovd.carbookingservice.dao.CarDao;
import com.github.matkovd.carbookingservice.exception.CarNotFoundException;
import com.github.matkovd.carbookingservice.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Statement;
import java.util.List;

@Service
public class MySQLCarDao implements CarDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Car> rowMapper;

    @Autowired
    public MySQLCarDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(Car.class);
    }

    @Override
    public List<Car> getAll() {
        var sql = "SELECT * FROM cars";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Long create(Car car) {
        var sql = "INSERT INTO cars (description, number) values (?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    final var preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, car.getDescription());
                    preparedStatement.setString(2, car.getNumber());
                    return preparedStatement;
                }, keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    @Override
    public Car getById(Long id) {
        try {
            var sql = "SELECT * FROM cars WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new CarNotFoundException();
        }
    }
}
