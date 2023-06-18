package com.jawojnar.jms.messagestructure;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageExpirationDemo {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");
        Queue expiryQueue = (Queue) context.lookup("queue/expiryQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory()) {
            JMSContext jmsContext = cf.createContext();

            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(2000L);
            producer.send((Destination) queue, "Ugghhhh");
            Thread.sleep(5000);

            Message messageReceived = jmsContext.createConsumer((Destination) queue).receive(5000L);
            System.out.println(messageReceived);

            System.out.println(jmsContext.createConsumer(expiryQueue).receiveBody(String.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
