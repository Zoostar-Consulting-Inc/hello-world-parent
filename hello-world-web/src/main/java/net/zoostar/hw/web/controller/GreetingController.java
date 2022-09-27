package net.zoostar.hw.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.web.request.StringValue;

@Slf4j
@RestController
@Secured({"ROLE_USER"})
public class GreetingController {

	@PostMapping(path = "/api/greet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StringValue> greet(@RequestBody StringValue name) {
		var response = new StringValue("Hello " + (StringUtils.hasText(name.toString()) ? name.toString() : "World"));
		log.info("Greeting user: {}", response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
