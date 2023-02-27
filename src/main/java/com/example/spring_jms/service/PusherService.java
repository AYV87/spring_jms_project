package com.example.spring_jms.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.rabbitmq.db.JmsRepository;
import org.example.spring.rabbitmq.jms.JmsTemplateProducer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PusherService {

    // бин репозитория
    private final JmsRepository jmsRepository;
    // бин отправителя
    private final JmsTemplateProducer jmsTemplateProducer;

    public PusherService(JmsRepository jmsRepository, JmsTemplateProducer jmsTemplateProducer) {
        this.jmsRepository = jmsRepository;
        this.jmsTemplateProducer = jmsTemplateProducer;
    }

    @SneakyThrows
    public void start() {
        // отправляем 5 сообщений с интервалом в 1 секунду
        for (int i = 0; i < 5; i++){
            TimeUnit.SECONDS.sleep(1);
            jmsTemplateProducer.sendMessage(i);
        }
        log.info("Waiting for all ActiveMQ JMS Messages to be consumed");
        TimeUnit.SECONDS.sleep(3);
        // вывести результаты из бд
        jmsRepository.showDataInDb();
        System.exit(0);
    }

}
