package jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

@Component
@Slf4j
public class JmsTemplateProducer {

    // бин с jms
    final JmsTemplate jmsTemplate;
    public JmsTemplateProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    // метод по отправке сообщений
    public void sendMessage(int i) {
        jmsTemplate.send(
                session -> {
                    TextMessage message = session.createTextMessage();
                    message.setText("Message #" + i); // само сообщение
                    message.setJMSType("my type"); // заголовок
                    log.info("sent: {} {}", message.getText(), message.getJMSType());
                    return message;
                }
        );
    }
}
