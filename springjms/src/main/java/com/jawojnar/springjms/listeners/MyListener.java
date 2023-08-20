package com.jawojnar.springjms.listeners;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Map;

@Component
public class MyListener {

    @JmsListener(destination = "${springjms.myQueue}")
    public void receive(Map<String,Integer> message) throws JMSException {
        System.out.println("Message received: " + message.get("abc"));
    }
}
