package com.api.contact;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.api.utils.Helper;

@Service
public class ContactService implements IContactService {
    @Autowired
    private ContactRepository contactRepo;

    @Autowired
    private Helper helper;

    @Override
    public Optional<Contact> create(Contact contact) {
        try {
            return Optional.of(this.contactRepo.save(contact));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return this.contactRepo.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.contactRepo.deleteById(id);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    @Override
    public Page<Contact> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword) {
        return this.contactRepo.findAll(helper.generatePageable(limit, page, sortBy, sortType));
    }

}
