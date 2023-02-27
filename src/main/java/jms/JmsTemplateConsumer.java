package jms;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.rabbitmq.db.JmsRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;


@Component
@Slf4j
public class JmsTemplateConsumer {

    // бин с бд
    private final JmsRepository jmsRepository;

    public JmsTemplateConsumer(JmsRepository jmsRepository) {
        this.jmsRepository = jmsRepository;
    }

    // метод для прослушивания очереди "empty"
    @JmsListener(destination = "empty")
    public void receiveMessage(TextMessage textMessage) {
        try {
            jmsRepository.writeMessageToDb(textMessage);
            log.info("received: {} {}", textMessage.getText(), textMessage.getJMSType());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in JmsTemplateConsumer class!!", e);
        }
    }
}

