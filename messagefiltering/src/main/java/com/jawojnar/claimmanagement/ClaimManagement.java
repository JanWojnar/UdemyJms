package com.jawojnar.claimmanagement;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClaimManagement {

    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue claimQueue = (Queue) initialContext.lookup("queue/claimQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext()){

            JMSProducer producer = jmsContext.createProducer();
//            JMSConsumer consumer = jmsContext.createConsumer(claimQueue, "hospitalId=1");
//            JMSConsumer consumer = jmsContext.createConsumer(claimQueue, "amount BETWEEN 900 AND 5000");
//            JMSConsumer consumer = jmsContext.createConsumer(claimQueue, "doctorName LIKE 'Joh_'");
            JMSConsumer consumer = jmsContext.createConsumer(claimQueue, "doctorType IN ('neuro','psych') OR JMSPriority BETWEEN 3 AND 6");

            ObjectMessage objectMessage = jmsContext.createObjectMessage();
//            objectMessage.setIntProperty("hospitalId", 1);
//            objectMessage.setDoubleProperty("amount", 5000);
//            objectMessage.setStringProperty("doctorName", "John");
            objectMessage.setStringProperty("doctorType", "gyna");

            Claim claim = Claim.builder().hospitalId(1).amount(1000.0).doctorName("John").doctorType("sryna").insuranceProvider("blue cross").build();
            objectMessage.setObject(claim);

            producer.send(claimQueue,objectMessage);

            Claim claim1 = consumer.receiveBody(Claim.class);
            System.out.println(claim1);


        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
