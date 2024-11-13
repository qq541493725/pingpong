package ping.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ping.util.FileLocker;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class Hello implements Runnable {
    @Scheduled(fixedDelay = 1500L)
    public void hello() {
        FileLocker.lock(this, () -> log.info("Request not send as being \"rate limited\"."));
    }

    @Override
    public void run() {
        WebClient.create("http://localhost:8080/world").get().retrieve()
                 .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals,
                         response -> Mono.error(new Exception("Request sent " + "&" + " Pong throttled it")))
                 .bodyToMono(String.class)
                 .subscribe(s -> log.info("Request sent & Pong Respond, {}", s), throwable -> log.info("{}",
                         throwable.getMessage()));
    }
}