package com.zidio.nexushr.attendance.repository;

//LeaveBalanceRepository.java


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zidio.nexushr.attendance.enums.LeaveType;
import com.zidio.nexushr.attendance.model.LeaveBalance;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    LeaveBalance findByEmployeeIdAndLeaveType(Long employeeId, LeaveType leaveType);
}
