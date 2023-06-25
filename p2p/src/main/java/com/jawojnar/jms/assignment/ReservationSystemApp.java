package com.jawojnar.jms.assignment;

import com.jawojnar.jms.hm.eligibility.listeners.EligibilityCheckerListener;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReservationSystemApp {

    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ) {
            JMSConsumer consumer1 = jmsContext.createConsumer(requestQueue);
            JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);

            consumer1.setMessageListener(new ReservationSystemListener(1));
            consumer2.setMessageListener(new ReservationSystemListener(2));


            Thread.sleep(100000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
