package eu.bigbank.dragons_of_mugloar.controller;

import eu.bigbank.dragons_of_mugloar.service.GameService;
import eu.bigbank.dragons_of_mugloar.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executor;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final SseService sseService;

    private final Executor executor;

    @GetMapping("/play")
    public SseEmitter play() {
        SseEmitter sseEmitter = sseService.getSseEmitter();
        executor.execute(() -> {
            try {
                gameService.play();
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }

}
