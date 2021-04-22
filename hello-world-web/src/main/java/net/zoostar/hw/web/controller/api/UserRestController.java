package net.zoostar.hw.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.model.User;
import net.zoostar.hw.service.UserService;
import net.zoostar.hw.validate.ValidatorException;
import net.zoostar.hw.web.request.UserRequest;

@Slf4j
@Getter
@Setter
@RestController
@NoArgsConstructor
@RequestMapping(value="/api/user")
public class UserRestController {

	@Autowired
	protected UserService userManager;
	
	@PostMapping(value="/create.json", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> create(@RequestBody UserRequest request) {
		log.info("Request received to create new: {}", request);
		
		ResponseEntity<User> response = null;
		var user = new User();
		try {
			user.setEmail(request.getEmail());
			user.setName(new StringBuilder().
					append(request.getFirstName()).
					append(" ").
					append(request.getLastName()).
					toString()
			);
			user = getUserManager().create(user);
			log.info("Created new entity: {}", user);
			response = new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (EntityAlreadyExistsException e) {
			log.warn("{}: {}", e.getMessage(), e.getEntity());
			response = new ResponseEntity<>(user, HttpStatus.EXPECTATION_FAILED);
		} catch (ValidatorException e) {
			log.warn("{}", e.getMessage());
			response = new ResponseEntity<>(user, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	@GetMapping(value="/page.json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<User>> retrieveUsers(@RequestParam int number, @RequestParam int limit) {
		return new ResponseEntity<>(getUserManager().retrieve(number, limit), HttpStatus.OK);
	}
	
	@GetMapping(value="/email.json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> retrieveByEmail(@RequestParam String email) {
		try {
			return new ResponseEntity<>(getUserManager().retrieveByEmail(email), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.warn("{}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
}
