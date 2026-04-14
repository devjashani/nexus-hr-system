package com.zidio.nexushr.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zidio.nexushr.common.model.Employee;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    Boolean existsByEmail(String email);
}