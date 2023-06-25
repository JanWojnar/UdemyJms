package com.jawojnar.jms.security;

import com.jawojnar.jms.hr.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecurityApp {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/empTopic");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ){
            jmsContext.setClientID("securityApp");

            JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            consumer.close(); // simulation of consumer crash

            Thread.sleep(10000L);

            consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            Message message = consumer.receive();
            Employee employee = message.getBody(Employee.class);
            System.out.println(employee.getFirstName());

            consumer.close();
            jmsContext.unsubscribe("subscription1");

        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
