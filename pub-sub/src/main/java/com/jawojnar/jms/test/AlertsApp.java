package com.jawojnar.jms.test;

import com.jawojnar.jms.test.model.CardTransaction;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AlertsApp {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/cardTopic");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            jmsContext.setClientID("alertsApp");

            JMSConsumer consumer1 = jmsContext.createSharedConsumer(topic, "consumerGroup1");
            JMSConsumer consumer2 = jmsContext.createSharedConsumer(topic, "consumerGroup1");
            JMSConsumer consumer3 = jmsContext.createSharedConsumer(topic, "consumerGroup1");

            Thread.sleep(15000);

            for(int i = 0 ; i<33 ; i++){
                Message receivedMessage1 = consumer1.receive();
                System.out.println("Consumer1" + receivedMessage1.getBody(CardTransaction.class));
                Message receivedMessage2 = consumer2.receive();
                System.out.println("Consumer2" + receivedMessage2.getBody(CardTransaction.class));
                Message receivedMessage3 = consumer3.receive();
                System.out.println("Consumer3" + receivedMessage3.getBody(CardTransaction.class));
            }
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
