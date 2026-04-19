package com.zidio.nexushr.attendance.repository;

//AttendanceRepository.java


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zidio.nexushr.attendance.model.Attendance;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
 
 Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
 
// List<Attendance> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate start, LocalDate end);
// 
// List<Attendance> findByEmployeeIdAndDateBetweenOrderByDateDesc(Long employeeId, LocalDate start, LocalDate end);


}






