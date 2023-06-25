package com.jawojnar.jms.welness;

import com.jawojnar.jms.hr.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class WelnessApp {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/empTopic");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            JMSConsumer consumer = jmsContext.createSharedConsumer(topic, "sharedConsumer");
            JMSConsumer consumer2 = jmsContext.createSharedConsumer(topic, "sharedConsumer");


            for(int i =0 ; i<10 ; i+=2){
                Message message = consumer.receive();
                Employee employee = message.getBody(Employee.class);
                System.out.println("Consumer1: " + employee.getFirstName());

                Message message2 = consumer2.receive();
                Employee employee2 = message.getBody(Employee.class);
                System.out.println("Consumer2: " + employee2.getFirstName());
            }

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
