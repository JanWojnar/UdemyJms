package com.jawojnar.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessagePropertiesDemo {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory()) {
            JMSContext jmsContext = cf.createContext();

            JMSProducer producer = jmsContext.createProducer();
            TextMessage textMessage = jmsContext.createTextMessage("Ugghhhh");
            textMessage.setBooleanProperty("loggedIn", true);
            textMessage.setStringProperty("userToken", "abc123");
            producer.send(queue, textMessage);

            Message messageReceived = jmsContext.createConsumer(queue).receive();
            System.out.println("Message received: " + messageReceived.getBody(String.class));
            System.out.println("Boolean property " + messageReceived.getBooleanProperty("loggedIn"));
            System.out.println("String property " + messageReceived.getStringProperty("userToken"));

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
