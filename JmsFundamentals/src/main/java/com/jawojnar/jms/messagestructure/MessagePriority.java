package com.jawojnar.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessagePriority {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");

        try(
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            JMSProducer producer = jmsContext.createProducer();
            String[] messages = new String[4];
            messages[0] = "Message One";
            messages[1] = "Message Two";
            messages[2] = "Message Three";
            messages[3] = "Message with default priority";

            producer.send(queue,messages[3]);

            producer.setPriority(3);
            producer.send(queue,messages[0]);

            producer.setPriority(4);
            producer.send(queue,messages[1]);

            producer.setPriority(9);
            producer.send(queue,messages[2]);


            JMSConsumer consumer = jmsContext.createConsumer(queue);

            for(int i = 0 ; i<4 ; i++) {
                Message receivedMessage = consumer.receive(5000L);
                System.out.println("Message priority: " + receivedMessage.getJMSPriority());
                System.out.println("Message body text: " + receivedMessage.getBody(String.class) + "\n");
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }


    }
}
