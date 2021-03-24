package com.github.matkovd.carbookingservice.dao;

import com.github.matkovd.carbookingservice.model.ContactDetails;

public interface ContactDetailsDao {
    Long create(ContactDetails contactDetails);
    Long getExactMatchId(ContactDetails contactDetails);
    ContactDetails getById(Long id);
}
