package com.jawojnar.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypesDemo {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory()) {
            JMSContext jmsContext = cf.createContext();

            JMSProducer producer = jmsContext.createProducer();
            // BytesMessage
            // BytesMessage bytesMessage = jmsContext.createBytesMessage();
            // bytesMessage.writeUTF("Jonas");
            // bytesMessage.writeLong(123L);

            // producer.send(queue, bytesMessage);

            // StreamMessage
            // StreamMessage streamMessage = jmsContext.createStreamMessage();
            // streamMessage.writeBoolean(true);
            // streamMessage.writeFloat(2.5F);
            //
            // producer.send(queue, streamMessage);

            // BytesMessage bytesMessageReceived = (BytesMessage) jmsContext.createConsumer(queue).receive();
            // System.out.println("Message received: " + bytesMessageReceived.readUTF());
            // System.out.println("Message received: " + bytesMessageReceived.readLong());

            // StreamMessage streamMessageReceived = (StreamMessage)
            // jmsContext.createConsumer(queue).receive();
            // System.out.println("Message received: " + streamMessageReceived.readBoolean());
            // System.out.println("Message received: " + streamMessageReceived.readFloat());

            MapMessage mapMessage = jmsContext.createMapMessage();
            mapMessage.setBoolean("isCreditAvailable", true);
            producer.send(queue, mapMessage);

            MapMessage mapMessageReceived = (MapMessage) jmsContext.createConsumer(queue).receive();
            System.out.println("Message received: " + mapMessageReceived.getBoolean("isCreditAvailable"));

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
