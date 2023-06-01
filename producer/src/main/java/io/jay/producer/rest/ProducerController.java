package io.jay.producer.rest;

import java.util.UUID;

import io.jay.producer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("producer")
@RequiredArgsConstructor
public class ProducerController {

	private final MyKafkaProducer producer;

	@GetMapping("/send")
	public void sendUsingSimpleProducer() {
		producer.sendMessage(UUID.randomUUID().toString());
	}
}
