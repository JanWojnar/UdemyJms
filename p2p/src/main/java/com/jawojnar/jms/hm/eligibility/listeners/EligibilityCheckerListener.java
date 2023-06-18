package com.jawojnar.jms.hm.eligibility.listeners;

import com.jawojnar.jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckerListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

        ObjectMessage objectMessage = (ObjectMessage) message;
        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
            MapMessage replyMessage = jmsContext.createMapMessage();

            Patient patient = (Patient) objectMessage.getObject();

            String insuraceProvider = patient.getInsuranceProvider();
            System.out.println(insuraceProvider);

            if(insuraceProvider.equals("Blue Cross Blue Shield") || insuraceProvider.equals("United Health")) {
                System.out.println("Patients Copay is : " + patient.getCopay());
                System.out.println("Patients amount to be paid  is : " + patient.getAmountToPay());
                if (patient.getCopay() <40 && patient.getAmountToPay()<1000){
                    replyMessage.setBoolean("eligible", true);
                }
            } else {
                replyMessage.setBoolean("eligible", false);
            }
            JMSProducer producer = jmsContext.createProducer();

            producer.send(replyQueue,replyMessage);

        } catch (JMSException | NamingException e) {
            throw new RuntimeException(e);
        }

    }
}
