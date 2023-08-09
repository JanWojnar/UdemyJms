package com.jawojnar;

import com.sun.tools.jconsole.JConsoleContext;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageConsumer {
    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
//                JMSContext jmsContext = cf.createContext()
//                JMSContext jmsContext = cf.createContext(JMSContext.CLIENT_ACKNOWLEDGE)
                JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)

        ){
            JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
            TextMessage message = (TextMessage) consumer.receive();
            jmsContext.commit();
            System.out.println(message.getText());

            message = (TextMessage) consumer.receive();
            System.out.println(message.getText());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}