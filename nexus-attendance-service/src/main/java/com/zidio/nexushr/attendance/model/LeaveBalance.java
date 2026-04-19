package com.zidio.nexushr.attendance.model;

import com.zidio.nexushr.attendance.enums.LeaveType;

//LeaveBalance.java

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "leave_balances")
public class LeaveBalance {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private LeaveType leaveType;

    private int totalLeaves;
    private int usedLeaves;
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	public int getTotalLeaves() {
		return totalLeaves;
	}
	public void setTotalLeaves(int totalLeaves) {
		this.totalLeaves = totalLeaves;
	}
	public int getUsedLeaves() {
		return usedLeaves;
	}
	public void setUsedLeaves(int usedLeaves) {
		this.usedLeaves = usedLeaves;
	}
	public int getRemainingLeaves() {
		return remainingLeaves;
	}
	public void setRemainingLeaves(int remainingLeaves) {
		this.remainingLeaves = remainingLeaves;
	}
	private int remainingLeaves;
}

//@Entity
//@Table(name = "leave_balances")
//@Data
//public class LeaveBalance {
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//private Long id;
//
//private Long employeeId;
//private Integer year;
//private BigDecimal annualLeave;
//private BigDecimal sickLeave;
//private BigDecimal casualLeave;
//private BigDecimal usedLeave;
//
//}

