package com.jawojnar.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RequestReplyDemo {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/requestQueue");
        // Queue replyQueue = (Queue) context.lookup("queue/replyQueue"); // Takes queue from Broker


        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory()){
            JMSContext jmsContext = cf.createContext();
            TemporaryQueue replyQueue = jmsContext.createTemporaryQueue(); // Dynamically created queue

            // App1
            JMSProducer producer = jmsContext.createProducer();
            TextMessage message = jmsContext.createTextMessage("Ugghhhh");
            message.setJMSReplyTo(replyQueue);
            producer.send(queue, message);
            System.out.println("App1 has sent message.");

            //App2
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage messageReceived = (TextMessage) consumer.receive();
            System.out.println("App2 has received message: " + messageReceived.getText());

            //App2
            JMSProducer replyProducer = jmsContext.createProducer();
            replyProducer.send(messageReceived.getJMSReplyTo(), "Ur awesome!");
            System.out.println("App2 has sent reply back.");

            //App1
            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
            System.out.println("App1 has received reply: " + replyConsumer.receiveBody(String.class));

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
