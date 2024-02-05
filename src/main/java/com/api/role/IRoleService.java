package com.api.role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IRoleService {
    public Optional<Role> create(Role role);

    public Set<Role> findByUserId(Long userId);

    public Optional<Role> findByName(String name);

    public List<Role> findAll();
}
