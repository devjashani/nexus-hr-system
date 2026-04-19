package com.zidio.nexushr.attendance.service;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zidio.nexushr.attendance.config.AttendancePublisher;
import com.zidio.nexushr.attendance.enums.AttendanceStatus;
import com.zidio.nexushr.attendance.model.Attendance;
import com.zidio.nexushr.attendance.repository.AttendanceRepository;
import com.zidio.nexushr.attendance.repository.HolidayRepository;

@Service
public class AttendanceService {

    @Autowired private AttendanceRepository repo;
    @Autowired private HolidayRepository holidayRepo;
    @Autowired private AttendancePublisher publisher;

    public String checkIn(Long employeeId) {

        LocalDate today = LocalDate.now();

        if (repo.findByEmployeeIdAndDate(employeeId, today).isPresent()) {
            return "Already checked in";
        }

        Attendance att = new Attendance();
        att.setEmployeeId(employeeId);
        att.setDate(today);
        att.setCheckIn(LocalDateTime.now());

        // Weekend
        DayOfWeek day = today.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            att.setStatus(AttendanceStatus.WEEKEND);
        }

        // Holiday
        if (holidayRepo.findByDate(today).isPresent()) {
            att.setStatus(AttendanceStatus.HOLIDAY);
        }

        repo.save(att);

        publisher.publish("Check-in: " + employeeId);
        return "Check-in successful";
    }

    public String checkOut(Long userId) {

        Attendance att = repo.findByEmployeeIdAndDate(userId, LocalDate.now())
                .orElseThrow();

        if (att.getCheckOut() != null) return "Already checked out";

        LocalDateTime out = LocalDateTime.now();
        att.setCheckOut(out);

        Duration d = Duration.between(att.getCheckIn(), out);
        double hours = d.toMinutes() / 60.0;

        att.setTotalHours(hours);

        if (hours > 8) att.setOvertimeHours(hours - 8);

        if (hours >= 8) att.setStatus(AttendanceStatus.PRESENT);
        else if (hours >= 4) att.setStatus(AttendanceStatus.HALF_DAY);

        repo.save(att);

        publisher.publish("Check-out: " + userId);
        return "Check-out successful";
    }
}