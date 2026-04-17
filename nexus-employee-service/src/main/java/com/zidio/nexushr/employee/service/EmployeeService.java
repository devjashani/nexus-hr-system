package com.zidio.nexushr.employee.service;

import java.util.List;
import java.util.UUID;

import com.zidio.nexushr.employee.dto.EmployeeRequest;
import com.zidio.nexushr.employee.dto.EmployeeResponse;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(UUID id);
    void deleteEmployee(UUID id);
}
