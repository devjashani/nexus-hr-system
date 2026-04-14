package com.zidio.nexushr.employee.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zidio.nexushr.employee.dto.EmployeeRequest;
import com.zidio.nexushr.employee.dto.EmployeeResponse;
import com.zidio.nexushr.employee.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public EmployeeResponse create(@RequestBody EmployeeRequest req) {
        return service.createEmployee(req);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public List<EmployeeResponse> getAll() {
        return service.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeResponse getById(@PathVariable UUID id) {
        return service.getEmployeeById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        service.deleteEmployee(id);
        return "Deleted Successfully";
    }
}
