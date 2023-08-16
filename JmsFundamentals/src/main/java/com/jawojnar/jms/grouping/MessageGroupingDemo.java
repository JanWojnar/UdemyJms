package com.jawojnar.jms.grouping;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.IllegalStateException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageGroupingDemo {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");
        Map<String, String> receivedMessages = new ConcurrentHashMap<>();

        try(
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
                JMSContext jmsContext2 = cf.createContext();
        ) {
            JMSProducer producer = jmsContext.createProducer();
            JMSConsumer consumer1 = jmsContext2.createConsumer(queue);
            consumer1.setMessageListener(new MyListener("Consumer-1", receivedMessages));
            JMSConsumer consumer2 = jmsContext2.createConsumer(queue);
            consumer2.setMessageListener(new MyListener("Consumer-2", receivedMessages));



            int count = 10;
            TextMessage[] messages = new TextMessage[count];
            for(int i = 0 ; i < count ; i++){
                messages[i] = jmsContext.createTextMessage("Group-0 message " + i);
                messages[i].setStringProperty("JMSXGroupID", "Group-0");
                producer.send(queue,messages[i]);
            }

            Thread.sleep(2000L);

            for(TextMessage message : messages){
                if(!receivedMessages.get(message.getText()).equals("Consumer-1")){
                    throw new IllegalStateException("Group Message " + message.getText() + "has gone to the wrong receiver!");
                }
            }

        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}

class MyListener implements MessageListener {

    private final String name;
    private final Map<String,String> receivedMessages;

    MyListener(String name, Map<String,String> receivedMessages){
        this.name = name;
        this.receivedMessages = receivedMessages;
    }
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("Message Received is " + textMessage.getText());
            System.out.println("Listener name " + name);
            receivedMessages.put(textMessage.getText(), name);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }
}
