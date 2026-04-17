package com.zidio.nexushr.payroll.controller;

import com.zidio.nexushr.payroll.model.Payroll;
import com.zidio.nexushr.payroll.model.PayrollStatus;
import com.zidio.nexushr.payroll.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollRepository payrollRepository;

    @GetMapping("/test")
    public String test() {
        return "✅ Payroll Service is Working!";
    }

    @GetMapping("/all")
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    @GetMapping("/employee/{employeeId}")
    public List<Payroll> getEmployeePayrolls(@PathVariable Long employeeId) {
        return payrollRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId);
    }

    // ✅ NEW: Process payroll for an employee
    @PostMapping("/process")
    public ResponseEntity<?> processPayroll(@RequestBody PayrollRequest request) {
        try {
            // Check if payroll already exists for this month
            var existing = payrollRepository.findByEmployeeIdAndMonthAndYear(
                request.getEmployeeId(), request.getMonth(), request.getYear());
            
            if (existing.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Payroll already processed for this month");
                return ResponseEntity.badRequest().body(error);
            }

            // Calculate salary
            Payroll payroll = calculateSalary(request);
            payrollRepository.save(payroll);
            
            return ResponseEntity.ok(payroll);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ✅ NEW: Calculate salary logic
    private Payroll calculateSalary(PayrollRequest request) {
        Payroll payroll = new Payroll();
        payroll.setEmployeeId(request.getEmployeeId());
        payroll.setEmployeeName(request.getEmployeeName());
        payroll.setEmployeeEmail(request.getEmployeeEmail());
        payroll.setMonth(request.getMonth());
        payroll.setYear(request.getYear());
        
        BigDecimal monthlySalary = request.getMonthlySalary();
        YearMonth yearMonth = YearMonth.of(request.getYear(), request.getMonth());
        int totalWorkingDays = yearMonth.lengthOfMonth() - 8; // Approx weekends off
        int presentDays = request.getPresentDays() != null ? request.getPresentDays() : totalWorkingDays;
        
        payroll.setTotalWorkingDays(totalWorkingDays);
        payroll.setPresentDays(presentDays);
        
        // Calculate per day salary
        BigDecimal perDaySalary = monthlySalary.divide(BigDecimal.valueOf(totalWorkingDays), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal lossOfPay = perDaySalary.multiply(BigDecimal.valueOf(totalWorkingDays - presentDays));
        
        // Earnings
        BigDecimal basicSalary = monthlySalary.multiply(new BigDecimal("0.40"));
        BigDecimal hra = basicSalary.multiply(new BigDecimal("0.50"));
        BigDecimal da = basicSalary.multiply(new BigDecimal("0.10"));
        BigDecimal ta = new BigDecimal("1600");
        BigDecimal totalEarnings = basicSalary.add(hra).add(da).add(ta).subtract(lossOfPay);
        
        // Deductions
        BigDecimal pf = basicSalary.multiply(new BigDecimal("0.12"));
        BigDecimal tds = calculateTDS(totalEarnings.multiply(new BigDecimal("12")));
        BigDecimal professionalTax = new BigDecimal("200");
        BigDecimal totalDeductions = pf.add(tds).add(professionalTax);
        
        // Net Salary
        BigDecimal netSalary = totalEarnings.subtract(totalDeductions);
        
        payroll.setBasicSalary(basicSalary);
        payroll.setHra(hra);
        payroll.setDa(da);
        payroll.setTa(ta);
        payroll.setTotalEarnings(totalEarnings);
        payroll.setPf(pf);
        payroll.setTds(tds);
        payroll.setProfessionalTax(professionalTax);
        payroll.setTotalDeductions(totalDeductions);
        payroll.setNetSalary(netSalary);
        payroll.setStatus(PayrollStatus.COMPLETED);
        payroll.setProcessedDate(LocalDate.now());
        
        return payroll;
    }

    private BigDecimal calculateTDS(BigDecimal annualSalary) {
        if (annualSalary.compareTo(new BigDecimal("250000")) <= 0) {
            return BigDecimal.ZERO;
        } else if (annualSalary.compareTo(new BigDecimal("500000")) <= 0) {
            return annualSalary.subtract(new BigDecimal("250000")).multiply(new BigDecimal("0.05"));
        } else if (annualSalary.compareTo(new BigDecimal("1000000")) <= 0) {
            BigDecimal tax1 = new BigDecimal("12500");
            BigDecimal tax2 = annualSalary.subtract(new BigDecimal("500000")).multiply(new BigDecimal("0.20"));
            return tax1.add(tax2);
        } else {
            BigDecimal tax1 = new BigDecimal("112500");
            BigDecimal tax2 = annualSalary.subtract(new BigDecimal("1000000")).multiply(new BigDecimal("0.30"));
            return tax1.add(tax2);
        }
    }

    // Inner class for request
    public static class PayrollRequest {
        private Long employeeId;
        private String employeeName;
        private String employeeEmail;
        private Integer month;
        private Integer year;
        private BigDecimal monthlySalary;
        private Integer presentDays;

        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public String getEmployeeEmail() { return employeeEmail; }
        public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }
        public Integer getMonth() { return month; }
        public void setMonth(Integer month) { this.month = month; }
        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }
        public BigDecimal getMonthlySalary() { return monthlySalary; }
        public void setMonthlySalary(BigDecimal monthlySalary) { this.monthlySalary = monthlySalary; }
        public Integer getPresentDays() { return presentDays; }
        public void setPresentDays(Integer presentDays) { this.presentDays = presentDays; }
    }
}
