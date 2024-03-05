package com.api.utils;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface ICrudService<T, K> {
    Optional<T> findById(K id);

    Optional<T> create(T body);

    Optional<T> update(K id, T body);

    boolean delete(K id);

    Page<T> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);

    boolean deleteMultiple(List<K> ids);
}
