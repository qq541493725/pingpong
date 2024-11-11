package pong.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PongTest extends Specification {
    @Autowired
    private Pong pong

    def "test"() {
        when:
        def result = pong.world()
        then:
        result == "World"
    }
}
