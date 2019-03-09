package com.example.gdp;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogConsumer {
    @RabbitListener(queues = GdpApplication.QUEUE_NAME)
    public void consumeMessage(final Message cm) {
        log.info("Received Message: {}", cm.toString());
    }
}
