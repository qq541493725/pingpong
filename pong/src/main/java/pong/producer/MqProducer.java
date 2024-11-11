package pong.producer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class MqProducer implements InitializingBean, DisposableBean {
    private DefaultMQProducer producer;
    private int i = 0;
    @Value("${ip:127.0.0.1}")
    private String ip;

    @Scheduled(fixedDelay = 500L)
    public void hello() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        SendResult result = producer.send(new Message("helloTopic", null, ("hello" + i).getBytes()));
        i++;
        SendStatus status = result.getSendStatus();
        log.info("mq send {}", status);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        producer = new DefaultMQProducer("helloGroup");
        producer.setNamesrvAddr(ip + ":9876");
        producer.start();
        log.info("mq start success {}", ip);
    }

    @Override
    public void destroy() {
        producer.shutdown();
    }
}
