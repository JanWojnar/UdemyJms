package com.jawojnar.jms.test;

import com.jawojnar.jms.hr.Employee;
import com.jawojnar.jms.test.model.CardTransaction;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQMapMessage;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDateTime;

public class CardApp {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/cardTopic");
        Queue replyQueue = (Queue) context.lookup("queue/replyQueue");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            JMSProducer producer = jmsContext.createProducer();
            JMSConsumer consumer = jmsContext.createConsumer(replyQueue);


            CardTransaction cardTransaction = CardTransaction.builder()
                    .time(LocalDateTime.now())
                    .amountOfMoney(5000.0)
                    .build();
            ObjectMessage message = jmsContext.createObjectMessage();
            message.setObject(cardTransaction);
            message.setJMSReplyTo(replyQueue);

            for(int i = 0 ; i<100 ; i++){
                producer.send(topic, message);
                System.out.println("Message with transaction:" + cardTransaction + " sent!");
            }
            Message receivedReplyMessage = consumer.receive(100000L);
            System.out.println("Transaction accepted: " + receivedReplyMessage.getBooleanProperty("accepted"));
            System.out.println(receivedReplyMessage.getBody(String.class));

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
