package com.api.contact;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface IContactService {
    Optional<Contact> create(Contact contact);

    Optional<Contact> findById(Long id);

    boolean delete(Long id);

    Page<Contact> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);
}
