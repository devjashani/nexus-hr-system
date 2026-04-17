package com.zidio.nexushr.payroll.controller;

import com.zidio.nexushr.payroll.model.Payroll;
import com.zidio.nexushr.payroll.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollRepository payrollRepository;

    @GetMapping("/test")
    public String test() {
        return "Payroll Service is Working! 🚀";
    }

    @GetMapping("/all")
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    @GetMapping("/employee/{employeeId}")
    public List<Payroll> getEmployeePayrolls(@PathVariable Long employeeId) {
        return payrollRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId);
    }
}