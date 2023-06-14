package com.jawojnar.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RequestReplyDemo {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) context.lookup("queue/replyQueue");


        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory()){
            JMSContext jmsContext = cf.createContext();

            JMSProducer producer = jmsContext.createProducer();
            producer.send(queue, "Ugghhhh");

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            String messageReceived = consumer.receiveBody(String.class);
            System.out.println(messageReceived);

            JMSProducer replyProducer = jmsContext.createProducer();
            replyProducer.send(replyQueue, "Ur awesome!");

            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
            System.out.println(consumer.receiveBody(String.class));
        }
    }
}
