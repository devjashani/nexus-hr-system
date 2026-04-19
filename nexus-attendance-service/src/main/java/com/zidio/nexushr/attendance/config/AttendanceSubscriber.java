package com.zidio.nexushr.attendance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zidio.nexushr.attendance.service.SseService;

@Component
public class AttendanceSubscriber {

    @Autowired 
    private SseService sseService;

    public void receive(String msg) {
        sseService.sendEvent(msg);
    }
}
