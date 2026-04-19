package com.zidio.nexushr.attendance.dto;

import jakarta.annotation.Nonnull;

//LeaveRequestRequest.java

//import jakarta.validation.constraints.Future;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

import com.zidio.nexushr.attendance.enums.LeaveType;

@Data
public class LeaveRequestRequest {
@Nonnull
 private LeaveType leaveType;
 
@Nonnull
// @Future(message = "Start date must be in the future")
 private LocalDate startDate;
 
@Nonnull
// @Future(message = "End date must be in the future")
 private LocalDate endDate;
 
// @Size(max = 500)
 private String reason;
 
 private String attachmentUrl;

 public String getAttachmentUrl() {
	return attachmentUrl;
 }

 public void setAttachmentUrl(String attachmentUrl) {
	this.attachmentUrl = attachmentUrl;
 }

 public String getReason() {
	return reason;
 }

 public void setReason(String reason) {
	this.reason = reason;
 }

 public LeaveType getLeaveType() {
	return leaveType;
 }

 public void setLeaveType(LeaveType leaveType) {
	this.leaveType = leaveType;
 }

 public LocalDate getStartDate() {
	return startDate;
 }

 public void setStartDate(LocalDate startDate) {
	this.startDate = startDate;
 }

 public LocalDate getEndDate() {
	return endDate;
 }

 public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
 }
}


