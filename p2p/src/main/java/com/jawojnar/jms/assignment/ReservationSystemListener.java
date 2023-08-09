package com.jawojnar.jms.assignment;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import javax.jms.*;

public class ReservationSystemListener implements MessageListener {

    private final int consumerId;
    public ReservationSystemListener(int consumerID) {
        this.consumerId=consumerID;
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
        ) {
            MapMessage replyMessage = jmsContext.createMapMessage();

            replyMessage.setJMSCorrelationID(message.getJMSMessageID());

            Passenger passenger = (Passenger) objectMessage.getObject();

            System.out.println("Consumer " + this.consumerId);
            System.out.println("Received message of messageId: " + objectMessage.getJMSMessageID());
            System.out.println("Received message of korrelationsId: " + objectMessage.getJMSCorrelationID());
            System.out.println(passenger.toString());

            replyMessage.setBoolean("reserved", true);

            JMSProducer producer = jmsContext.createProducer();

            producer.send(message.getJMSReplyTo(), replyMessage);

            message.acknowledge();

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
