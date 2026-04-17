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
    
    @Column(nullable = false)
    private Long employeeId;
    
    private String employeeEmail;
    private String employeeName;
    
    @Column(nullable = false)
    private Integer month;
    
    @Column(nullable = false)
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
    
    // Attendance
    private Integer presentDays;
    private Integer totalWorkingDays;
    
    @Enumerated(EnumType.STRING)
    private PayrollStatus status;
    
    private LocalDate processedDate;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public Payroll() {}
    
    // Getters
    public Long getId() { return id; }
    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeEmail() { return employeeEmail; }
    public String getEmployeeName() { return employeeName; }
    public Integer getMonth() { return month; }
    public Integer getYear() { return year; }
    public BigDecimal getBasicSalary() { return basicSalary; }
    public BigDecimal getHra() { return hra; }
    public BigDecimal getDa() { return da; }
    public BigDecimal getTa() { return ta; }
    public BigDecimal getTotalEarnings() { return totalEarnings; }
    public BigDecimal getPf() { return pf; }
    public BigDecimal getTds() { return tds; }
    public BigDecimal getProfessionalTax() { return professionalTax; }
    public BigDecimal getTotalDeductions() { return totalDeductions; }
    public BigDecimal getNetSalary() { return netSalary; }
    public Integer getPresentDays() { return presentDays; }
    public Integer getTotalWorkingDays() { return totalWorkingDays; }
    public PayrollStatus getStatus() { return status; }
    public LocalDate getProcessedDate() { return processedDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public void setMonth(Integer month) { this.month = month; }
    public void setYear(Integer year) { this.year = year; }
    public void setBasicSalary(BigDecimal basicSalary) { this.basicSalary = basicSalary; }
    public void setHra(BigDecimal hra) { this.hra = hra; }
    public void setDa(BigDecimal da) { this.da = da; }
    public void setTa(BigDecimal ta) { this.ta = ta; }
    public void setTotalEarnings(BigDecimal totalEarnings) { this.totalEarnings = totalEarnings; }
    public void setPf(BigDecimal pf) { this.pf = pf; }
    public void setTds(BigDecimal tds) { this.tds = tds; }
    public void setProfessionalTax(BigDecimal professionalTax) { this.professionalTax = professionalTax; }
    public void setTotalDeductions(BigDecimal totalDeductions) { this.totalDeductions = totalDeductions; }
    public void setNetSalary(BigDecimal netSalary) { this.netSalary = netSalary; }
    public void setPresentDays(Integer presentDays) { this.presentDays = presentDays; }
    public void setTotalWorkingDays(Integer totalWorkingDays) { this.totalWorkingDays = totalWorkingDays; }
    public void setStatus(PayrollStatus status) { this.status = status; }
    public void setProcessedDate(LocalDate processedDate) { this.processedDate = processedDate; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
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
