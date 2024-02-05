package com.api.role;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Set<Role> findByUsers_id(Long userId);
    
    public Optional<Role> findByName(String name);
}
