package com.zidio.nexushr.attendance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AttendancePublisher {

    @Autowired 
    private RedisTemplate<String, Object> redisTemplate;

    public void publish(String msg) {
        redisTemplate.convertAndSend("attendance-channel", msg);
    }
}