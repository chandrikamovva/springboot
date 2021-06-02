package com.example.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crud.model.ERole;
import com.example.crud.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
	Optional<Role> findByName(ERole name);

}
