package ping.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MqConsumer implements InitializingBean, DisposableBean {
    private DefaultMQPushConsumer consumer;

    @Value("${ip:127.0.0.1}")
    private String ip;

    @Override
    public void destroy() {
        consumer.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        consumer = new DefaultMQPushConsumer("helloGroup");
        consumer.setNamesrvAddr(ip + ":9876");
        consumer.subscribe("helloTopic", "*");
        consumer.registerMessageListener(new Listener());
        consumer.start();
    }

    static class Listener implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                        ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            for (MessageExt msg : list) {
                log.info("mq receive '{}'", new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}
