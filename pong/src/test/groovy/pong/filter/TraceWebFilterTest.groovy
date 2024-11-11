package pong.filter


import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import spock.lang.Specification

class TraceWebFilterTest extends Specification {
    def "test"() {
        setup:
        def filter = new TraceWebFilter()
        def monoEmpty = Mono.empty()
        def exchange = Mock(ServerWebExchange)
        def response = Mock(ServerHttpResponse)
        response.writeWith(Mono.empty()) >> monoEmpty
        exchange.getResponse() >> response
        def chain = Mock(WebFilterChain)
        def monoNext = Mono.empty()
        chain.filter(exchange) >> monoNext

        when:
        def result1 = filter.filter(exchange, chain)
        def result2 = filter.filter(exchange, chain)

        then:
        result1 == monoNext
        result2 == monoEmpty
    }
}