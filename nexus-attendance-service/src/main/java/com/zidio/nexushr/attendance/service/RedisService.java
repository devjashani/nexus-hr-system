//package com.zidio.nexushr.attendance.service;
//
////RedisService.java
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zidio.nesushr.attendance.model.AttendanceEvent;
//import com.zidio.nesushr.attendance.model.LeaveEvent;

//import com.zidio.nexushr.attendance.event.AttendanceEvent;
//import com.zidio.nexushr.attendance.event.LeaveEvent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.concurrent.TimeUnit;



//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class RedisService {
// 
// private final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
// private final ChannelTopic attendanceTopic;
// private final ChannelTopic leaveTopic;
// private final ChannelTopic notificationTopic;
// private final ObjectMapper objectMapper = new ObjectMapper();
// 
// // ============ CACHE METHODS ============
// 
// // Cache attendance data
// public void cacheAttendance(String key, Object data, Duration ttl) {
//     try {
//         redisTemplate.opsForValue().set(key, data, ttl);
//         log.debug("Cached attendance data for key: {}", key);
//     } catch (Exception e) {
//         log.error("Error caching data: {}", e.getMessage());
//     }
// }
// 
// // Get cached attendance
// public Object getCachedAttendance(String key) {
//     try {
//         return redisTemplate.opsForValue().get(key);
//     } catch (Exception e) {
//         log.error("Error getting cached data: {}", e.getMessage());
//         return null;
//     }
// }
// 
// // Cache today's attendance for employee
// public void cacheTodayAttendance(Long employeeId, Object attendance) {
//     String key = "attendance:today:" + employeeId;
//     cacheAttendance(key, attendance, Duration.ofHours(24));
// }
// 
// // Get today's attendance from cache
// public Object getTodayAttendanceFromCache(Long employeeId) {
//     String key = "attendance:today:" + employeeId;
//     return getCachedAttendance(key);
// }
// 
// // Cache leave balance
// public void cacheLeaveBalance(Long employeeId, Integer year, Object balance) {
//     String key = String.format("leave:balance:%d:%d", employeeId, year);
//     cacheAttendance(key, balance, Duration.ofDays(7));
// }
// 
// // Cache employee session (check-in status)
// public void setEmployeeSession(Long employeeId, String sessionData) {
//     String key = "session:employee:" + employeeId;
//     redisTemplate.opsForValue().set(key, sessionData, 8, TimeUnit.HOURS);
// }
// 
// public Object getEmployeeSession(Long employeeId) {
//     String key = "session:employee:" + employeeId;
//     return redisTemplate.opsForValue().get(key);
// }
// 
// // Delete cache (on update)
// public void evictAttendanceCache(Long employeeId) {
//     String pattern = "attendance:*:" + employeeId;
//     // In production, use SCAN for production
//     redisTemplate.delete(pattern);
//     log.info("Evicted attendance cache for employee: {}", employeeId);
// }
// 
// // ============ PUBLISH METHODS ============
// 
// // Publish attendance event
// public void publishAttendanceEvent(AttendanceEvent event) {
//     try {
//         String eventJson = objectMapper.writeValueAsString(event);
//         redisTemplate.convertAndSend(attendanceTopic.getTopic(), eventJson);
//         log.info("Published attendance event: {} for employee: {}", 
//             event.getEventType(), event.getEmployeeId());
//     } catch (JsonProcessingException e) {
//         log.error("Error publishing attendance event: {}", e.getMessage());
//     }
// }
// 
// // Publish leave event
// public void publishLeaveEvent(LeaveEvent event) {
//     try {
//         String eventJson = objectMapper.writeValueAsString(event);
//         redisTemplate.convertAndSend(leaveTopic.getTopic(), eventJson);
//         log.info("Published leave event: {} for request: {}", 
//             event.getLeaveStatus(), event.getLeaveRequestId());
//     } catch (JsonProcessingException e) {
//         log.error("Error publishing leave event: {}", e.getMessage());
//     }
// }
// 
// // Publish notification
// public void publishNotification(String channel, Object notification) {
//     try {
//         String notificationJson = objectMapper.writeValueAsString(notification);
//         redisTemplate.convertAndSend(channel, notificationJson);
//         log.info("Published notification to channel: {}", channel);
//     } catch (JsonProcessingException e) {
//         log.error("Error publishing notification: {}", e.getMessage());
//     }
// }
// 
// // ============ SUBSCRIBER METHODS (called by listeners) ============
// 
// // Handle attendance events from other services
// public void handleAttendanceEvent(String message) {
//     try {
//         AttendanceEvent event = objectMapper.readValue(message, AttendanceEvent.class);
//         log.info("Received attendance event: {} for employee: {}", 
//             event.getEventType(), event.getEmployeeId());
//         // Process event - update local cache, send notifications, etc.
//         processAttendanceEvent(event);
//     } catch (JsonProcessingException e) {
//         log.error("Error parsing attendance event: {}", e.getMessage());
//     }
// }
// 
// // Handle leave events from other services
// public void handleLeaveEvent(String message) {
//     try {
//         LeaveEvent event = objectMapper.readValue(message, LeaveEvent.class);
//         log.info("Received leave event: {} for request: {}", 
//             event.getLeaveStatus(), event.getLeaveRequestId());
//         processLeaveEvent(event);
//     } catch (JsonProcessingException e) {
//         log.error("Error parsing leave event: {}", e.getMessage());
//     }
// }
// 
// private void processAttendanceEvent(AttendanceEvent event) {
//     // Invalidate caches when attendance is updated from other services
//     if (event.getEventType().equals("CHECK_IN") || event.getEventType().equals("CHECK_OUT")) {
//         evictAttendanceCache(event.getEmployeeId());
//     }
// }
// 
// private void processLeaveEvent(LeaveEvent event) {
//     // Handle leave events from other services
//     if (event.getLeaveStatus().equals("APPROVED") || event.getLeaveStatus().equals("REJECTED")) {
//         String key = String.format("leave:balance:%d:%d", 
//             event.getEmployeeId(), java.time.LocalDate.now().getYear());
//         redisTemplate.delete(key);
//     }
// }
//}
