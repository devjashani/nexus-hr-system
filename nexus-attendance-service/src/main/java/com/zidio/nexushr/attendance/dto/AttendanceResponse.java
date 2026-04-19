package com.zidio.nexushr.attendance.dto;

//AttendanceResponse.java

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

import com.zidio.nexushr.attendance.enums.AttendanceStatus;

@Data
@Builder
public class AttendanceResponse {
private Long id;
private Long employeeId;
private LocalDate date;
private LocalTime checkInTime;
private LocalTime checkOutTime;
private AttendanceStatus status;
private Integer lateMinutes;
private Integer overtimeMinutes;
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
public LocalTime getCheckInTime() {
	return checkInTime;
}
public void setCheckInTime(LocalTime checkInTime) {
	this.checkInTime = checkInTime;
}
public LocalDate getDate() {
	return date;
}
public void setDate(LocalDate date) {
	this.date = date;
}
public Integer getLateMinutes() {
	return lateMinutes;
}
public void setLateMinutes(Integer lateMinutes) {
	this.lateMinutes = lateMinutes;
}
public LocalTime getCheckOutTime() {
	return checkOutTime;
}
public void setCheckOutTime(LocalTime checkOutTime) {
	this.checkOutTime = checkOutTime;
}
public AttendanceStatus getStatus() {
	return status;
}
public void setStatus(AttendanceStatus status) {
	this.status = status;
}
public Integer getOvertimeMinutes() {
	return overtimeMinutes;
}
public void setOvertimeMinutes(Integer overtimeMinutes) {
	this.overtimeMinutes = overtimeMinutes;
}
}
