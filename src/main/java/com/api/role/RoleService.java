package com.api.role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public Optional<Role> create(Role role) {
        try {
            return Optional.ofNullable(roleRepo.save(role));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<Role> findByUserId(Long userId) {
        return roleRepo.findByUsers_id(userId);
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            return roleRepo.findByName(name);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
}
