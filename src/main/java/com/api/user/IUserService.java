package com.api.user;

import java.util.Optional;

public interface IUserService {
    public Optional<User> findByEmail(String email);

    public Optional<User> create(User user);

    public long countAll();
}
