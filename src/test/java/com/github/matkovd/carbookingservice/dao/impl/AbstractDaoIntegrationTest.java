package com.github.matkovd.carbookingservice.dao.impl;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.github.matkovd.carbookingservice.constants.Constants.MYSQL_IMAGE;

abstract class AbstractDaoIntegrationTest {
    protected static final Long CAR_ID = 36L;
    protected static final Long CONTACT_DETAILS_ID = 17L;

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("car_booking")
            .withUsername("car_booking")
            .withPassword("password");


    static JdbcTemplate jdbcTemplate() {
        var dataSource = new MysqlDataSource();
        dataSource.setUrl(mysql.getJdbcUrl());
        dataSource.setUser(mysql.getUsername());
        dataSource.setPassword(mysql.getPassword());
        return new JdbcTemplate(dataSource);
    }

    static void loadSqlFile(JdbcTemplate jdbcTemplate, String filename) {
        var resourceDatabasePopulator =
                new ResourceDatabasePopulator(
                        false,
                        false,
                        "UTF-8",
                        new ClassPathResource(filename)
                );
        resourceDatabasePopulator.execute(jdbcTemplate.getDataSource());
    }

    static void putDatabaseToInitialState(JdbcTemplate jdbcTemplate) {
        loadSqlFile(jdbcTemplate, "restore-db.sql");
    }
}
