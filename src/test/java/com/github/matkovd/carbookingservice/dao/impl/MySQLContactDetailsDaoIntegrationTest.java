package com.github.matkovd.carbookingservice.dao.impl;

import com.github.matkovd.carbookingservice.model.ContactDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
class MySQLContactDetailsDaoIntegrationTest extends AbstractDaoIntegrationTest {

    private static final ContactDetails CONTACT_DETAILS =
            new ContactDetails(0L, "test", "test", "123", "71112225566");

    private static MySQLContactDetailsDao contactDetailsDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() {
        jdbcTemplate = jdbcTemplate();
        loadSqlFile(jdbcTemplate, "schema.sql");
        loadSqlFile(jdbcTemplate, "data.sql");
        contactDetailsDao = new MySQLContactDetailsDao(jdbcTemplate);
    }

    @Test
    void create_ValidContactDetails_CreatesSuccessfully() {
        var id = contactDetailsDao.create(CONTACT_DETAILS);
        var details = contactDetailsDao.getById(id);
        assertEquals(details.getName(), CONTACT_DETAILS.getName());
        assertEquals(details.getSurname(), CONTACT_DETAILS.getSurname());
        assertEquals(details.getIdNumber(), CONTACT_DETAILS.getIdNumber());
        assertEquals(details.getPhone(), CONTACT_DETAILS.getPhone());
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void getById_NonExistingEntity_ReturnsNull() {
        var details = contactDetailsDao.getById(99L);
        assertNull(details);
    }

    @Test
    void getExactMatch_DoesntMatch_ReturnsNull() {
        contactDetailsDao.create(CONTACT_DETAILS);
        var details = contactDetailsDao.getExactMatchId(
                new ContactDetails(0L, "some", "random", "23", "71112225566")
        );
        assertNull(details);
        putDatabaseToInitialState(jdbcTemplate);
    }

    @Test
    void getExactMatch_MatchesRecord_ReturnsSuccessfully() {
        var id = contactDetailsDao.create(CONTACT_DETAILS);
        var matchId = contactDetailsDao.getExactMatchId(CONTACT_DETAILS);
        assertEquals(id, matchId);
        putDatabaseToInitialState(jdbcTemplate);
    }
}
