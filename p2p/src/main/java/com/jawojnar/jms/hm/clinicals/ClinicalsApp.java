package com.jawojnar.jms.hm.clinicals;

import com.jawojnar.jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClinicalsApp {
    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replytQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            JMSProducer producer = jmsContext.createProducer();

            ObjectMessage objectMessage= jmsContext.createObjectMessage();
            Patient patient = new Patient();
            patient.setId(123);
            patient.setName("Jan");
            patient.setInsuranceProvider("Blue Cross Blue Shield");
            patient.setCopay(30d);
            patient.setAmountToPay(500d);

            objectMessage.setObject(patient);

//            for(int i = 1 ; i<11 ; i++) {
//
//            }
            producer.send(requestQueue,objectMessage);

            JMSConsumer consumer = jmsContext.createConsumer(replytQueue);
            MapMessage replyMessage = (MapMessage) consumer.receive(30000L);
            System.out.println("Patient eligibility is: " + replyMessage.getBoolean("eligible"));
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}