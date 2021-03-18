package net.zoostar.hw.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.MissingRequiredFieldException;
import net.zoostar.hw.service.UserService;
import net.zoostar.hw.web.dto.User;

@Slf4j
@Getter
@ToString
@RestController
@NoArgsConstructor
@RequestMapping(value="/api/user")
public class UserRestController {

	@Autowired
	protected UserService userManager;
	
	public UserRestController(UserService userManager) {
		this.userManager = userManager;
	}

	@PostMapping(value="/create.json", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> create(@RequestBody User user) {
		ResponseEntity<User> response = null;
		try {
			net.zoostar.hw.model.User entity = new net.zoostar.hw.model.User(user.getEmail());
			entity.setName(user.getName());
			entity = getUserManager().create(entity);
			log.info("Created new entity: {}", entity);
			response = new ResponseEntity<>(user, HttpStatus.OK);
		} catch (EntityAlreadyExistsException e) {
			log.warn("{}: {}", e.getMessage(), e.getEntity());
			response = new ResponseEntity<>(user, HttpStatus.EXPECTATION_FAILED);
		} catch (MissingRequiredFieldException e) {
			log.warn("{}", e.getMessage());
			response = new ResponseEntity<>(user, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
}
