package com.jawojnar.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {

    public static void main(String[] args) throws NamingException, JMSException {


        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/myTopic");

        ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = cf.createConnection();

        Session session = connection.createSession();

        MessageProducer producer = session.createProducer(topic);
        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageConsumer consumer2 =session.createConsumer(topic);

        TextMessage message = session.createTextMessage("Hi children!");

        producer.send(message);
        connection.start();

        TextMessage message1 = (TextMessage) consumer1.receive(5000L);
        TextMessage message2 = (TextMessage) consumer2.receive(5000L);

        System.out.println("Consumer1 received: " + message1.getText());
        System.out.println("Consumer2 received: " + message2.getText());

        connection.close();
        initialContext.close();
    }
}
