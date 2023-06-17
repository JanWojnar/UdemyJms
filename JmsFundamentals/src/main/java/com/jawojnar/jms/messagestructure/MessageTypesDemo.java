package com.jawojnar.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
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

            // MapMessage
            // MapMessage mapMessage = jmsContext.createMapMessage();
            // mapMessage.setBoolean("isCreditAvailable", true);
            // producer.send(queue, mapMessage);

            // MapMessage mapMessageReceived = (MapMessage) jmsContext.createConsumer(queue).receive();
            // System.out.println("Message received: " + mapMessageReceived.getBoolean("isCreditAvailable"));

            // ObjectMessage objectMessage = jmsContext.createObjectMessage();
            // Patient patient = new Patient();
            // patient.setId(123);
            // patient.setName("Kowalski");
            // objectMessage.setObject(patient);
            // producer.send(queue, objectMessage);

            Patient patient = new Patient();
            patient.setId(123);
            patient.setName("Kowalski");
            producer.send(queue, patient);

            // ObjectMessage objectMessageReceived = (ObjectMessage)
            // jmsContext.createConsumer(queue).receive();
            // Patient patientReceived = (Patient) objectMessageReceived.getObject();
            // System.out.println("Message received: " + patientReceived.getId());
            // System.out.println("Message received: " + patientReceived.getName());

            Patient patientReceived = jmsContext.createConsumer(queue).receiveBody(Patient.class);
            System.out.println("Message received: " + patientReceived.getId());
            System.out.println("Message received: " + patientReceived.getName());

        }
    }
}
