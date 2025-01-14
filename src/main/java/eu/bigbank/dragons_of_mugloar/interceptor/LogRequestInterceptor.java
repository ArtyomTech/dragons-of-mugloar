package eu.bigbank.dragons_of_mugloar.interceptor;

import eu.bigbank.dragons_of_mugloar.service.SseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LogRequestInterceptor implements HandlerInterceptor {

    private final ApplicationContext applicationContext;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        SseService sseService = applicationContext.getBean(SseService.class);
        sseService.createSseEmitter();
        return true;
    }

}
