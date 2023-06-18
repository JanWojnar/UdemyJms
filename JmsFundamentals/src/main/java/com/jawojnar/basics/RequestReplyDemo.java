package com.jawojnar.basics;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

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
            System.out.println(message.getJMSMessageID());
            System.out.println("App1 has sent message.");

            Map<String, TextMessage> requestMessages = new HashMap<>();
            requestMessages.put(message.getJMSMessageID(), message);

            //App2
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage messageReceived = (TextMessage) consumer.receive();
            System.out.println("App2 has received message: " + messageReceived.getText());

            //App2
            JMSProducer replyProducer = jmsContext.createProducer();
            TextMessage replyMessage = jmsContext.createTextMessage("Reply Message!!!");
            replyMessage.setJMSCorrelationID(messageReceived.getJMSMessageID());
            replyProducer.send(messageReceived.getJMSReplyTo(), replyMessage);
            System.out.println("App2 has sent reply back.");

            //App1
            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
            TextMessage replyReceived = (TextMessage) replyConsumer.receive();
            System.out.println("App1 has received reply: " + replyReceived.getText());
            System.out.println("Received reply CorrelationID: " + replyReceived.getJMSCorrelationID());

            System.out.println(
                "Reply received by App1 has the same correlationsId as messageId of initially sent message." +
                    " When we look for the message saved in map with correlationId we find the same message: "
                    + requestMessages.get(replyReceived.getJMSCorrelationID()).getText());

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
