package com.api.user;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByEmail(String email);

    Optional<User> create(User user);

    long countAll();

    Optional<User> update(User user);

    Author convertUserToAuthor(User user);

    User convertAuthorToUser(Author author);
}
