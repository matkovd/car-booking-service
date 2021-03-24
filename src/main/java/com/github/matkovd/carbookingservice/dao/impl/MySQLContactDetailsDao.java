package com.github.matkovd.carbookingservice.dao.impl;

import com.github.matkovd.carbookingservice.dao.ContactDetailsDao;
import com.github.matkovd.carbookingservice.model.ContactDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Statement;

@Service
public class MySQLContactDetailsDao implements ContactDetailsDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ContactDetails> rowMapper;

    @Autowired
    public MySQLContactDetailsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(ContactDetails.class);
    }


    @Override
    public Long create(ContactDetails contactDetails) {
        var sql = "INSERT INTO contact_details (name, surname, id_number, phone) values (?, ?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    final var preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, contactDetails.getName());
                    preparedStatement.setString(2, contactDetails.getSurname());
                    preparedStatement.setString(3, contactDetails.getIdNumber());
                    preparedStatement.setString(4, contactDetails.getPhone());
                    return preparedStatement;
                }, keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    @Override
    public ContactDetails getById(Long id) {
        try {
            var sql = "SELECT * FROM contact_details WHERE id = ?";
            return jdbcTemplate.queryForObject(
                    sql,
                    rowMapper,
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Long getExactMatchId(ContactDetails contactDetails) {
        try {
            var sql = "SELECT id FROM contact_details WHERE name = ? AND surname = ? AND id_number = ? AND phone = ?";
            return jdbcTemplate.queryForObject(
                    sql,
                    Long.class,
                    contactDetails.getName(),
                    contactDetails.getSurname(),
                    contactDetails.getIdNumber(),
                    contactDetails.getPhone()
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
