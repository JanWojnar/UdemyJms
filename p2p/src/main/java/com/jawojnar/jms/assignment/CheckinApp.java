package com.jawojnar.jms.assignment;

import com.jawojnar.jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

public class CheckinApp {

    public static void main (String[] args)throws NamingException{

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED);
        ){
            JMSProducer producer = jmsContext.createProducer();
            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);

            for(int i = 0 ; i<3 ; i++) {

                Passenger passenger = new Passenger();
                passenger.setFirstName("Jan");
                passenger.setLastName("Wojnar " + i+1);
                passenger.setEmail("rudydodomu@gmail.com");
                passenger.setPhone("213 721 372");

                ObjectMessage objectMessage = jmsContext.createObjectMessage();
                passenger.setId(LocalDateTime.now().getNano());
                objectMessage.setObject(passenger);
                objectMessage.setJMSReplyTo(replyQueue);
                objectMessage.setObject(passenger);

                producer.send(requestQueue,objectMessage);

                if(i!=2){
                    jmsContext.commit();
                    System.out.println("CheckinApp sending message with messageId: " + objectMessage.getJMSMessageID());

                } else {
                    jmsContext.rollback();
                }
            }

            MapMessage replyMessage = null;
            do{
                replyMessage = (MapMessage) replyConsumer.receive(10000L);
                if(Objects.nonNull(replyMessage)){
                    System.out.println("Passenger reservation successful: " + replyMessage.getBoolean("reserved"));
                }
            } while (Objects.nonNull(replyMessage));


    } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
