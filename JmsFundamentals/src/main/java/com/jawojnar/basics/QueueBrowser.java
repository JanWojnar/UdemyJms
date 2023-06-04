package com.jawojnar.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowser {
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

            TextMessage message1 = session.createTextMessage("Ohhh");
            TextMessage message2 = session.createTextMessage("Ahhh");

            producer.send(message1);
            producer.send(message2);

            javax.jms.QueueBrowser browser = session.createBrowser(queue);

            Enumeration messagesEnum = browser.getEnumeration();

            System.out.println();
            while(messagesEnum.hasMoreElements()){
                TextMessage eachMessage = (TextMessage) messagesEnum.nextElement();
                System.out.println("Browsing: " + eachMessage.getText());
            }

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();

            System.out.println("\nAfter browsing:\n");

            TextMessage messageReceived = (TextMessage) consumer.receive(5000L);
            System.out.println(messageReceived.getText());
            messageReceived = (TextMessage) consumer.receive(5000L);
            System.out.println(messageReceived.getText());
            System.out.println("\nMessages was browsed without consuming them");


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