package com.api.subscriber;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.utils.Helper;

@Service
public class SubscriberService implements ISubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepo;

    @Autowired
    private Helper helper;

    @Override
    public Optional<Subscriber> create(Subscriber subscriber) {
        try {
            return Optional.of(this.subscriberRepo.save(subscriber));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Subscriber> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return this.subscriberRepo.findAll(pageable);
    }

    @Override
    public Boolean delete(Long id) {
        try {
            this.subscriberRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
