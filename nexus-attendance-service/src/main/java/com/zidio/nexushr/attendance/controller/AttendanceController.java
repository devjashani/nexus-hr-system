package com.zidio.nexushr.attendance.controller;

//AttendanceController.java

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.zidio.nexushr.attendance.service.AttendanceService;
import com.zidio.nexushr.attendance.service.SseService;

@RestController
@RequestMapping("api/attendance")
public class AttendanceController {

    @Autowired private AttendanceService service;
    @Autowired private SseService sseService;

    @PostMapping("/checkin/{userId}")
    public String checkIn(@PathVariable Long userId) {
        return service.checkIn(userId);
    }

    @PostMapping("/checkout/{userId}")
    public String checkOut(@PathVariable Long userId) {
        return service.checkOut(userId);
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        return sseService.subscribe();
    }
}

//@RestController
//@RequestMapping("/api/attendance")
//@RequiredArgsConstructor
//public class AttendanceController {
// 
// private final AttendanceService attendanceService = new AttendanceService();
// 
// @PostMapping("/check-in")
// public ResponseEntity<?> checkIn(
//         @RequestParam Long employeeId,
//         @RequestParam String employeeName) {
//     return ResponseEntity.ok(attendanceService.checkIn(employeeId, employeeName));
// }
// 
// @PostMapping("/check-out")
// public ResponseEntity<?> checkOut(
//         @RequestParam Long employeeId,
//         @RequestParam String employeeName) {
//     return ResponseEntity.ok(attendanceService.checkOut(employeeId, employeeName));
// }
// 
// @GetMapping("/today")
// public ResponseEntity<?> getToday(@RequestParam Long employeeId) {
//     return ResponseEntity.ok(attendanceService.getTodayAttendance(employeeId));
// }
//}
//
