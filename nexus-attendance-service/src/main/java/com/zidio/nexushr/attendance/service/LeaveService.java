package com.zidio.nexushr.attendance.service;

import com.zidio.nexushr.attendance.config.AttendancePublisher;
import com.zidio.nexushr.attendance.dto.LeaveRequestRequest;
import com.zidio.nexushr.attendance.enums.LeaveStatus;
import com.zidio.nexushr.attendance.model.LeaveBalance;
import com.zidio.nexushr.attendance.model.LeaveRequest;

//EnhancedLeaveService.java

import com.zidio.nexushr.attendance.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class LeaveService {

    @Autowired private LeaveRepository leaveRepo;
    @Autowired private LeaveBalanceRepository balanceRepo;
    @Autowired private AttendancePublisher publisher;

    public String applyLeave(LeaveRequestRequest dto, Long employeeId) {

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(employeeId);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setLeaveType(dto.getLeaveType());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.PENDING);
        leave.setAppliedDate(LocalDateTime.now());

        leaveRepo.save(leave);

        publisher.publish("Leave applied by user " + employeeId);

        return "Leave applied successfully";
    }

    public String approveLeave(Long leaveId) {

        LeaveRequest leave = leaveRepo.findById(leaveId).orElseThrow();

        leave.setStatus(LeaveStatus.APPROVED);

        long days = ChronoUnit.DAYS.between(
                leave.getStartDate(), leave.getEndDate()) + 1;

        LeaveBalance balance = balanceRepo
                .findByEmployeeIdAndLeaveType(leave.getEmployeeId(), leave.getLeaveType());

        balance.setUsedLeaves(balance.getUsedLeaves() + (int) days);
        balance.setRemainingLeaves(
                balance.getTotalLeaves() - balance.getUsedLeaves());

        leaveRepo.save(leave);
        balanceRepo.save(balance);

        publisher.publish("Leave approved for user " + leave.getEmployeeId());

        return "Leave approved";
    }

    public List<LeaveRequest> getMyLeaves(Long EmployeeId) {
        return leaveRepo.findByEmployeeId(EmployeeId);
    }
}
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class LeaveService {
// 
// private final LeaveRequestRepository leaveRequestRepository;
// private final LeaveBalanceRepository leaveBalanceRepository;
// private final RedisService redisService;
// private final SseService sseService;
// 
// @Transactional
// public Map<String, Object> applyLeave(Long employeeId, String employeeName, 
//                                       String leaveType, LocalDate startDate, 
//                                       LocalDate endDate, String reason) {
//     
//     long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
//     BigDecimal requestedDays = BigDecimal.valueOf(days);
//     
//     int year = LocalDate.now().getYear();
//     
//     // Try to get balance from cache
//     String cacheKey = String.format("leave:balance:%d:%d", employeeId, year);
//     LeaveBalance balance = (LeaveBalance) redisService.getCachedAttendance(cacheKey);
//     
//     if (balance == null) {
//         balance = leaveBalanceRepository.findByEmployeeIdAndYear(employeeId, year)
//             .orElseGet(() -> createDefaultBalance(employeeId, year));
//         redisService.cacheLeaveBalance(employeeId, year, balance);
//     }
//     
//     BigDecimal available = getAvailableBalance(balance, leaveType);
//     
//     if (requestedDays.compareTo(available) > 0) {
//         throw new RuntimeException("Insufficient leave balance. Available: " + available);
//     }
//     
//     LeaveRequest leaveRequest = new LeaveRequest();
//     leaveRequest.setEmployeeId(employeeId);
//     leaveRequest.setLeaveType(leaveType);
//     leaveRequest.setStartDate(startDate);
//     leaveRequest.setEndDate(endDate);
//     leaveRequest.setReason(reason);
//     leaveRequest.setStatus("PENDING");
//     leaveRequest = leaveRequestRepository.save(leaveRequest);
//     
//     // Publish event
//     LeaveEvent event = new LeaveEvent();
//     event.setEventId(UUID.randomUUID().toString());
//     event.setEventType("APPLIED");
//     event.setLeaveRequestId(leaveRequest.getId());
//     event.setEmployeeId(employeeId);
//     event.setTimestamp(LocalDateTime.now());
//     event.setStatus("PENDING");
//     event.setMessage("Leave request submitted");
//     redisService.publishLeaveEvent(event);
//     
//     // Send SSE to managers
//     Map<String, Object> notification = new HashMap<>();
//     notification.put("type", "LEAVE_APPLIED");
//     notification.put("leaveRequestId", leaveRequest.getId());
//     notification.put("employeeId", employeeId);
//     notification.put("employeeName", employeeName);
//     notification.put("leaveType", leaveType);
//     notification.put("days", days);
//     sseService.broadcastToAll("leave_notification", notification);
//     
//     // Send confirmation to employee
//     sseService.sendToEmployee(employeeId, "leave_applied", 
//         Map.of("message", "Leave request submitted", "requestId", leaveRequest.getId()));
//     
//     Map<String, Object> result = new HashMap<>();
//     result.put("message", "Leave request submitted successfully");
//     result.put("requestId", leaveRequest.getId());
//     result.put("status", "PENDING");
//     return result;
// }
// 
// @Transactional
// public Map<String, Object> approveLeave(Long requestId, Long approverId, String approverName) {
//     LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
//         .orElseThrow(() -> new RuntimeException("Leave request not found"));
//     
//     if (!"PENDING".equals(leaveRequest.getStatus())) {
//         throw new RuntimeException("Leave request already processed");
//     }
//     
//     long days = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
//     BigDecimal usedDays = BigDecimal.valueOf(days);
//     
//     int year = leaveRequest.getStartDate().getYear();
//     LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndYear(leaveRequest.getEmployeeId(), year)
//         .orElseThrow(() -> new RuntimeException("Leave balance not found"));
//     
//     updateBalance(balance, leaveRequest.getLeaveType(), usedDays);
//     balance = leaveBalanceRepository.save(balance);
//     
//     // Update cache
//     redisService.cacheLeaveBalance(leaveRequest.getEmployeeId(), year, balance);
//     
//     leaveRequest.setStatus("APPROVED");
//     leaveRequest.setApprovedBy(approverId);
//     leaveRequest.setApprovedAt(LocalDateTime.now());
//     leaveRequest = leaveRequestRepository.save(leaveRequest);
//     
//     // Publish event
//     LeaveEvent event = new LeaveEvent();
//     event.setEventId(UUID.randomUUID().toString());
//     event.setEventType("APPROVED");
//     event.setLeaveRequestId(requestId);
//     event.setEmployeeId(leaveRequest.getEmployeeId());
//     event.setManagerId(approverId);
//     event.setTimestamp(LocalDateTime.now());
//     event.setStatus("APPROVED");
//     event.setMessage("Leave request approved by " + approverName);
//     redisService.publishLeaveEvent(event);
//     
//     // Send SSE to employee
//     sseService.sendToEmployee(leaveRequest.getEmployeeId(), "leave_approved", 
//         Map.of("message", "Your leave request has been approved", 
//                "requestId", requestId,
//                "approvedBy", approverName));
//     
//     Map<String, Object> result = new HashMap<>();
//     result.put("message", "Leave approved successfully");
//     return result;
// }
// 
// @Transactional
// public Map<String, Object> rejectLeave(Long requestId, Long approverId, String approverName, String reason) {
//     LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
//         .orElseThrow(() -> new RuntimeException("Leave request not found"));
//     
//     leaveRequest.setStatus("REJECTED");
//     leaveRequest.setApprovedBy(approverId);
//     leaveRequest.setApprovedAt(LocalDateTime.now());
//     leaveRequest = leaveRequestRepository.save(leaveRequest);
//     
//     // Publish event
//     LeaveEvent event = new LeaveEvent();
//     event.setEventId(UUID.randomUUID().toString());
//     event.setEventType("REJECTED");
//     event.setLeaveRequestId(requestId);
//     event.setEmployeeId(leaveRequest.getEmployeeId());
//     event.setManagerId(approverId);
//     event.setTimestamp(LocalDateTime.now());
//     event.setStatus("REJECTED");
//     event.setMessage("Leave request rejected by " + approverName + ". Reason: " + reason);
//     redisService.publishLeaveEvent(event);
//     
//     // Send SSE to employee
//     sseService.sendToEmployee(leaveRequest.getEmployeeId(), "leave_rejected", 
//         Map.of("message", "Your leave request has been rejected", 
//                "requestId", requestId,
//                "reason", reason));
//     
//     Map<String, Object> result = new HashMap<>();
//     result.put("message", "Leave rejected");
//     return result;
// }
// 
// private LeaveBalance createDefaultBalance(Long employeeId, int year) {
//     LeaveBalance balance = new LeaveBalance();
//     balance.setEmployeeId(employeeId);
//     balance.setYear(year);
//     balance.setAnnualLeave(BigDecimal.valueOf(20));
//     balance.setSickLeave(BigDecimal.valueOf(12));
//     balance.setCasualLeave(BigDecimal.valueOf(5));
//     balance.setUsedLeave(BigDecimal.ZERO);
//     return leaveBalanceRepository.save(balance);
// }
// 
// private BigDecimal getAvailableBalance(LeaveBalance balance, String leaveType) {
//     switch (leaveType) {
//         case "ANNUAL": return balance.getAnnualLeave().subtract(balance.getUsedLeave());
//         case "SICK": return balance.getSickLeave();
//         case "CASUAL": return balance.getCasualLeave();
//         default: return BigDecimal.valueOf(999);
//     }
// }
// 
// private void updateBalance(LeaveBalance balance, String leaveType, BigDecimal days) {
//     balance.setUsedLeave(balance.getUsedLeave().add(days));
// }
//}
