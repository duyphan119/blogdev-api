package com.api.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return userRepo.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> create(User user) {
        try {
            return Optional.ofNullable(userRepo.save(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public long countAll() {
        return userRepo.count();
    }
    
}
