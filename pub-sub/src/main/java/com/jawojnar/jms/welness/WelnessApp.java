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
            JMSConsumer consumer = jmsContext.createConsumer(topic);
            Message message = consumer.receive();
            Employee employee = message.getBody(Employee.class);

            System.out.println(employee.getFirstName());

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
