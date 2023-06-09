package com.jawojnar.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
public class FirstQueue {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();

            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

            connection = cf.createConnection();

            Session session = connection.createSession();

            Queue queue = (Queue) initialContext.lookup("queue/myQueue");

            MessageProducer producer = session.createProducer(queue);

            TextMessage message = session.createTextMessage("I am creator!!!");

            producer.send(message);

            System.out.println("Message Sent: " + message.getText());

            MessageConsumer consumer1 = session.createConsumer(queue);
            MessageConsumer consumer2 = session.createConsumer(queue);

            connection.start();

            TextMessage messageReceived = (TextMessage) consumer1.receive(5000L);
            System.out.println("Message Received by consumer 1: " + messageReceived.getText());

            TextMessage messageReceivedd = (TextMessage) consumer2.receive(5000L);
            try{
                System.out.println("Message Received by consumer 2: " + messageReceivedd.getText());
            } catch (NullPointerException np){
                System.out.println("Second consumer in session does not receive message, because it was consumed in this session by first consumer!");
            }

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}