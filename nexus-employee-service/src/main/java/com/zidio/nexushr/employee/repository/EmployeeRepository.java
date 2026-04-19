package com.zidio.nexushr.employee.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zidio.nexushr.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
	boolean existsByEmail(String email);
}
