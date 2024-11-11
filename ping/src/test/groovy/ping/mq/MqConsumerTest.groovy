package ping.mq

import org.apache.rocketmq.common.message.MessageExt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@SpringBootTest
class MqConsumerTest extends Specification {
    @Autowired
    MqConsumer consumer;

    def "test"() {
        expect:
        TimeUnit.SECONDS.sleep(1)
    }

    def "listener"() {
        def con = new MqConsumer.Listener()
        def msg = Mock(MessageExt)
        msg.getBody() >> "hello".getBytes()
        expect:
        con.consumeMessage(Arrays.asList(msg), null)
    }
}
