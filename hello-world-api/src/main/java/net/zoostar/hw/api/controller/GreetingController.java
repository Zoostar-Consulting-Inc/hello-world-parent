package net.zoostar.hw.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.api.transformer.impl.GreetingResponse;
import net.zoostar.hw.api.transformer.impl.GreetingResponseTransformer;

@Slf4j
@RestController
@RequestMapping("/api")
public class GreetingController {

	public static final String GREETING_MESSAGE = "Hello World";

	@GetMapping("/greet")
	public ResponseEntity<GreetingResponse> greet() {
		String message = GREETING_MESSAGE;
		log.info("Greet with message: {}", message);
		return ResponseEntity.ok(new GreetingResponseTransformer(message).transform());
	}
}
