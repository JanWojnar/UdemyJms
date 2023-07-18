package com.jawojnar.jms.test;

import com.jawojnar.jms.test.model.CardTransaction;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecurityApp {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/cardTopic");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            jmsContext.setClientID("securityApp");
            JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscriber1");
            JMSProducer producer = jmsContext.createProducer();

            consumer.close();
            System.out.println("CRASH");
            Thread.sleep(10000);
            System.out.println("WAKE UP!!! Back online.");
            consumer = jmsContext.createDurableConsumer(topic, "subscription1");

            Message receivedMessage = consumer.receive();
            CardTransaction receivedTransaction = receivedMessage.getBody(CardTransaction.class);
            System.out.println("Received with transaction: " + receivedTransaction + " !");

            Message replyMessage = jmsContext.createMapMessage();
            replyMessage.setJMSCorrelationID(receivedMessage.getJMSMessageID());

            if(receivedTransaction.getAmountOfMoney() > 6000) {
                replyMessage.setBooleanProperty("accepted", false);
                producer.send(receivedMessage.getJMSReplyTo(), "Transaction denied! Amount over the limit!");
            } else {
                replyMessage.setBooleanProperty("accepted", true);
                producer.send(receivedMessage.getJMSReplyTo(), "Transaction accepted!");
            }
            System.out.println("Reply sent back!");
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
