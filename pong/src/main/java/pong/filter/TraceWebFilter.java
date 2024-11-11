package pong.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TraceWebFilter implements WebFilter {
    private static LocalDateTime time;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        synchronized (this) {
            LocalDateTime now = LocalDateTime.now();
            if (time != null) {
                long delay = ChronoUnit.SECONDS.between(now, time);
                if (delay == 0) {
                    ServerHttpResponse resp = exchange.getResponse();
                    resp.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                    return resp.writeWith(Mono.empty());
                }
            }
            time = now;
        }
        return chain.filter(exchange);
    }
}