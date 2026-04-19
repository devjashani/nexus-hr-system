package com.zidio.nexushr.employee.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zidio.nexushr.employee.dto.EmployeeRequest;
import com.zidio.nexushr.employee.dto.EmployeeResponse;
import com.zidio.nexushr.employee.model.Employee;
import com.zidio.nexushr.employee.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee emp = new Employee();
        emp.setFirstName(request.firstname);
        emp.setLastName(request.lastname);
        emp.setEmail(request.email);
        emp.setRole(request.role);
        emp.setSalary(request.salary);

        emp = repo.save(emp);

        EmployeeResponse res = new EmployeeResponse();
        res.id = emp.getId();
        res.firstname = emp.getFirstName();
        res.lastname = emp.getLastName();
        res.email = emp.getEmail();
        res.role = emp.getRole();

        return res;
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return repo.findAll().stream().map(emp -> {
            EmployeeResponse res = new EmployeeResponse();
            res.id = emp.getId();
            res.firstname = emp.getFirstName();
            res.lastname= emp.getLastName();
            res.email = emp.getEmail();
            res.role = emp.getRole();
            return res;
        }).toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(UUID id) {
        Employee emp = repo.findById(id)
        		.orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeResponse res = new EmployeeResponse();
        res.id = emp.getId();
        res.firstname = emp.getFirstName();
        res.lastname =emp.getLastName();
        res.email = emp.getEmail();
        res.role = emp.getRole();

        return res;
    }

    @Override
    public void deleteEmployee(UUID id) {
        repo.deleteById(id);
    }
}
