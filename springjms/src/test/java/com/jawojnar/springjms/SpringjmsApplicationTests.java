package com.jawojnar.springjms;

import com.jawojnar.springjms.senders.MessageSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringjmsApplicationTests {

	@Autowired
	MessageSender messageSender;
	@Test
	void contextLoads() {

		Map<String,Integer>  message = new HashMap<>();
		message.put("abc", 123);
		messageSender.send(message);
	}

}
