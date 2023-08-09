package com.jawojnar;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageProducer {
    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED);
        ){
            JMSProducer producer = jmsContext.createProducer();
            producer.send(requestQueue, "Message1");
            producer.send(requestQueue, "Message2");
            jmsContext.commit();
        }
    }
}