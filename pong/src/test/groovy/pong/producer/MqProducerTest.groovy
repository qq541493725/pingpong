package pong.producer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class MqProducerTest extends Specification {
    @Autowired
    MqProducer producer;

    def "test"() {
        when:
        producer.hello()

        then:
        producer.getI() > 0
    }
}