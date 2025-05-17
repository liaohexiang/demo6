package com.example.demo6;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RestController
public class SseController {
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        SseEmitter emitter = new SseEmitter(50000L);

        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().data("Server time: " + System.currentTimeMillis()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);
        emitter.onCompletion(() -> {
            future.cancel(true);
            executor.shutdownNow();
        });
        // 超时处理
        emitter.onTimeout(() -> {
            emitter.complete();
            future.cancel(true);
            executor.shutdownNow();
        });
        return emitter;
    }
}