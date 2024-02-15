package com.api.subscriber;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface ISubscriberService {
    Optional<Subscriber> create(Subscriber subscriber);

    Page<Subscriber> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);

    boolean delete(Long id);
}
