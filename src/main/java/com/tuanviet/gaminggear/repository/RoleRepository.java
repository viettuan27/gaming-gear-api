package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.auth.Role;
import com.tuanviet.gaminggear.entity.auth.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName name);
}
