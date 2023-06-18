package com.jawojnar.jms.hm.eligibility;

import com.jawojnar.jms.hm.eligibility.listeners.EligibilityCheckerListener;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckerApp {

    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
            consumer.setMessageListener(new EligibilityCheckerListener());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
