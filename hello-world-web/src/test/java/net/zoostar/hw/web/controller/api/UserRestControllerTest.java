package net.zoostar.hw.web.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.dao.hsql.UserRepository;
import net.zoostar.hw.model.User;
import net.zoostar.hw.service.impl.UserServiceImpl;

@Slf4j
@ActiveProfiles({"test"})
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-test.xml"})
class UserRestControllerTest {
	
	static final int PAGE_LIMIT = 3;
	
	List<User> users;
	
	@Mock
	UserRepository repository;
	
	UserRestController controller;

	@BeforeEach
	public void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());

		users = Collections.unmodifiableList(new ObjectMapper().readValue(
				new ClassPathResource("data/users.json").getInputStream(), new TypeReference<List<User>>() { }));
		log.info("Total number of users loaded: {}", users.size());

		controller = new UserRestController(new UserServiceImpl(repository));
	}
	
	@Test
	void testCreateSuccess() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String id = UUID.randomUUID().toString();
		User entity = user(0, 1);
		Mockito.when(controller.getUserManager().getRepository().save(entity)).
			thenReturn(savedEntity(id, entity));

		//WHEN
		net.zoostar.hw.web.dto.User user = new net.zoostar.hw.web.dto.User();
		user.setEmail(entity.getEmail());
		user.setName(entity.getName());
		ResponseEntity<net.zoostar.hw.web.dto.User> response = controller.create(user);
		
		//THEN
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		user = response.getBody();
		assertNotNull(user);
		log.info("Retrieved entity: {}", user);
		assertEquals(entity.getEmail(), user.getEmail());
		assertEquals(entity.getName(), user.getName());
	}

	@Test
	void testCreateWithBlankEmail() throws JsonParseException, JsonMappingException, IOException {
		//WHEN
		net.zoostar.hw.web.dto.User user = new net.zoostar.hw.web.dto.User();
		ResponseEntity<net.zoostar.hw.web.dto.User> response = controller.create(user);
		
		//THEN
		assertNotNull(response);
		assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
		user = response.getBody();
		assertNotNull(user);
		log.info("Retrieved entity: {}", user);
	}

	@Test
	void testCreateWithExistingEmail() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String id = UUID.randomUUID().toString();
		User entity = user(0, 1);
		entity.setId(id);
		Mockito.when(controller.getUserManager().getRepository().findByEmail(entity.getEmail())).
			thenReturn(savedEntity(id, entity));
		
		//WHEN
		net.zoostar.hw.web.dto.User user = new net.zoostar.hw.web.dto.User();
		user.setEmail(entity.getEmail());
		user.setName(entity.getName());
		ResponseEntity<net.zoostar.hw.web.dto.User> response = controller.create(user);
		
		//THEN
		assertNotNull(response);
		assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
		user = response.getBody();
		assertNotNull(user);
		log.info("Retrieved entity: {}", user);
		assertEquals(entity.getEmail(), user.getEmail());
		assertEquals(entity.getName(), user.getName());
	}
	
	protected User user(int number, int limit) throws JsonParseException, JsonMappingException, IOException {
		PageRequest request = PageRequest.of(number, limit);
		return page(request, limit, users.size()).getContent().get(0);
	}

	protected User savedEntity(String id, User user) {
		User entity = new User(user.getEmail());
		entity.setId(id);
		entity.setName(user.getName());
		return entity;
	}

	protected Page<User> page(PageRequest page, int limit, int size) throws JsonParseException, JsonMappingException, IOException {
		int from = page.getPageNumber() * limit;
		int to = from + limit;
		if(to > size) {
			to = from + (size % from);
		}
		log.info("Given page number {} and page size {}...", (page.getPageNumber() + 1), limit);
		log.info("Then return a total of {}/{} products from {} to {}.", (to - from), size, (from + 1), to);
		return new PageImpl<>(users.subList(from, to), page, users.size());
	}

}
