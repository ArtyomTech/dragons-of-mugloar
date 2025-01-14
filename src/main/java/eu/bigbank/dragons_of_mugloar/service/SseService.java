package eu.bigbank.dragons_of_mugloar.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Getter
@Service
public class SseService {

    private SseEmitter sseEmitter;

    public void send(String message) {
        SseEmitter.SseEventBuilder event = SseEmitter.event().data(message);
        try {
            sseEmitter.send(event);
            log.info(message);
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
            log.error(e.getMessage());
        }
    }

    public void complete() {
        sseEmitter.complete();
    }

    public void createSseEmitter() {
        this.sseEmitter = new SseEmitter();
    }

}
