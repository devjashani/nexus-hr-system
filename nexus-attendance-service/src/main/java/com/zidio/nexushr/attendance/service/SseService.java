package com.zidio.nexushr.attendance.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        return emitter;
    }

    public void sendEvent(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(message);
            } catch (Exception e) {
                emitter.complete();
            }
        }
    }
}