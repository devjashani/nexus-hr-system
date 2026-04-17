package com.zidio.nexushr.payroll.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payrolls")
public class Payroll {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long employeeId;
    private String employeeEmail;
    private String employeeName;
    private Integer month;
    private Integer year;
    
    // Earnings
    private BigDecimal basicSalary;
    private BigDecimal hra;
    private BigDecimal da;
    private BigDecimal ta;
    private BigDecimal totalEarnings;
    
    // Deductions
    private BigDecimal pf;
    private BigDecimal tds;
    private BigDecimal professionalTax;
    private BigDecimal totalDeductions;
    
    // Net
    private BigDecimal netSalary;
    
    private Integer presentDays;
    private Integer totalWorkingDays;
    
    @Enumerated(EnumType.STRING)
    private PayrollStatus status;
    
    private LocalDate processedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    
    public String getEmployeeEmail() { return employeeEmail; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public BigDecimal getBasicSalary() { return basicSalary; }
    public void setBasicSalary(BigDecimal basicSalary) { this.basicSalary = basicSalary; }
    
    public BigDecimal getHra() { return hra; }
    public void setHra(BigDecimal hra) { this.hra = hra; }
    
    public BigDecimal getDa() { return da; }
    public void setDa(BigDecimal da) { this.da = da; }
    
    public BigDecimal getTa() { return ta; }
    public void setTa(BigDecimal ta) { this.ta = ta; }
    
    public BigDecimal getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(BigDecimal totalEarnings) { this.totalEarnings = totalEarnings; }
    
    public BigDecimal getPf() { return pf; }
    public void setPf(BigDecimal pf) { this.pf = pf; }
    
    public BigDecimal getTds() { return tds; }
    public void setTds(BigDecimal tds) { this.tds = tds; }
    
    public BigDecimal getProfessionalTax() { return professionalTax; }
    public void setProfessionalTax(BigDecimal professionalTax) { this.professionalTax = professionalTax; }
    
    public BigDecimal getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(BigDecimal totalDeductions) { this.totalDeductions = totalDeductions; }
    
    public BigDecimal getNetSalary() { return netSalary; }
    public void setNetSalary(BigDecimal netSalary) { this.netSalary = netSalary; }
    
    public Integer getPresentDays() { return presentDays; }
    public void setPresentDays(Integer presentDays) { this.presentDays = presentDays; }
    
    public Integer getTotalWorkingDays() { return totalWorkingDays; }
    public void setTotalWorkingDays(Integer totalWorkingDays) { this.totalWorkingDays = totalWorkingDays; }
    
    public PayrollStatus getStatus() { return status; }
    public void setStatus(PayrollStatus status) { this.status = status; }
    
    public LocalDate getProcessedDate() { return processedDate; }
    public void setProcessedDate(LocalDate processedDate) { this.processedDate = processedDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}