package com.zidio.nexushr.attendance.controller;

import com.zidio.nexushr.attendance.dto.LeaveRequestRequest;
import com.zidio.nexushr.attendance.model.LeaveRequest;
import com.zidio.nexushr.attendance.service.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/leave")
@RequiredArgsConstructor
public class LeaveController {

    @Autowired private LeaveService service;

    @PostMapping("/apply/{employeeId}")
    public String applyLeave(@RequestBody LeaveRequestRequest dto,
                             @PathVariable Long employeeId) {
        return service.applyLeave(dto, employeeId);
    }

    @PutMapping("/approve/{leaveId}")
    public String approveLeave(@PathVariable Long leaveId) {
        return service.approveLeave(leaveId);
    }

    @GetMapping("/my/{employeeId}")
    public List<LeaveRequest> myLeaves(@PathVariable Long employeeId) {
        return service.getMyLeaves(employeeId);
    }
}

//@RestController
//@RequestMapping("/api/leave")
//@RequiredArgsConstructor
//public class LeaveController {
// 
// private final LeaveService leaveService = new LeaveService();
// 
// @PostMapping("/apply")
// public ResponseEntity<?> applyLeave(
//         @RequestParam Long employeeId,
//         @RequestParam String employeeName,
//         @RequestParam String leaveType,
//         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//         @RequestParam(required = false) String reason) {
//     return ResponseEntity.ok(leaveService.applyLeave(employeeId, employeeName, 
//         leaveType, startDate, endDate, reason));
// }
// 
// @PostMapping("/approve/{requestId}")
// public ResponseEntity<?> approveLeave(
//         @PathVariable Long requestId,
//         @RequestParam Long approverId,
//         @RequestParam String approverName) {
//     return ResponseEntity.ok(leaveService.approveLeave(requestId, approverId, approverName));
// }
// 
// @PostMapping("/reject/{requestId}")
// public ResponseEntity<?> rejectLeave(
//         @PathVariable Long requestId,
//         @RequestParam Long approverId,
//         @RequestParam String approverName,
//         @RequestParam String reason) {
//     return ResponseEntity.ok(leaveService.rejectLeave(requestId, approverId, approverName, reason));
// }
//}
//
