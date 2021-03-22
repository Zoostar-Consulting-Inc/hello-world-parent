package net.zoostar.hw.web.controller.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import net.zoostar.hw.service.UserService;
import net.zoostar.hw.service.impl.UserServiceImpl;

@Slf4j
@ActiveProfiles({"test"})
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-test.xml"})
class UserRestControllerTest {
	
	static final int PAGE_LIMIT = 3;
	
	List<User> entities;
	
	@Mock
	UserRepository repository;
	
	UserRestController controller;

	@BeforeEach
	public void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());

		entities = entities();
		log.info("Total number of users loaded: {}", entities.size());

		UserService userManager = new UserServiceImpl();
		userManager.setRepository(repository);
		controller = new UserRestController();
		controller.setUserManager(userManager);
	}
	
	protected List<User> entities() throws JsonParseException, JsonMappingException, IOException {
		return Collections.unmodifiableList(new ObjectMapper().readValue(
				new ClassPathResource("data/users.json").getInputStream(),
				new TypeReference<List<User>>() { }));
	}

	@Test
	void testCreateSuccess() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String id = UUID.randomUUID().toString();
		User entity = entity(0, 1);
		Mockito.when(controller.getUserManager().getRepository().save(entity)).
			thenReturn(savedEntity(id, entity));

		//WHEN
		User user = new User();
		user.setEmail(entity.getEmail());
		user.setName(entity.getName());
		ResponseEntity<User> response = controller.create(user);
		
		//THEN
		assertTrue(user.isNew());
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		user = response.getBody();
		assertNotNull(user);
		log.info("Retrieved entity: {}", user);
		assertEquals(entity.getEmail(), user.getEmail());
		assertEquals(entity.getName(), user.getName());
		assertTrue(entity.equals(entity));
		assertTrue(user.equals(entity));
		assertFalse(entity.isNew());
		assertFalse(entity.equals(new User("random@email.com")));
		assertFalse(entity.equals(null));
		assertFalse(entity.equals(new Object()));
	}

	@Test
	void testCreateWithBlankEmail() throws JsonParseException, JsonMappingException, IOException {
		//WHEN
		User user = new User();
		ResponseEntity<User> response = controller.create(user);
		
		//THEN
		assertNotNull(response);
		assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
		user = response.getBody();
		assertNotNull(user);
		log.info("Retrieved entity: {}", user);
	}

	@Test
	void testCreateWithExistingId() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String id = UUID.randomUUID().toString();
		User user = entity(0, 1);
		user.setId(id);
		
		//WHEN
		ResponseEntity<User> response = controller.create(user);
		
		//THEN
		assertNotNull(response);
		assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
		User entity = response.getBody();
		assertNotNull(entity);
		log.info("Retrieved entity: {}", user);
		assertEquals(user.getEmail(), entity.getEmail());
		assertEquals(user.getName(), entity.getName());
	}

	@Test
	void testCreateWithExistingEmail() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String id = UUID.randomUUID().toString();
		User entity = entity(0, 1);
		entity.setId(id);
		Mockito.when(controller.getUserManager().getRepository().findByEmail(entity.getEmail())).
			thenReturn(savedEntity(id, entity));
		
		//WHEN
		User user = new User();
		user.setEmail(entity.getEmail());
		user.setName(entity.getName());
		ResponseEntity<User> response = controller.create(user);
		
		//THEN
		assertNotNull(response);
		assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
		user = response.getBody();
		assertNotNull(user);
		log.info("Retrieved entity: {}", user);
		assertEquals(entity.getEmail(), user.getEmail());
		assertEquals(entity.getName(), user.getName());
	}

	@Test
	void testRetrieveFailureForInvalidPageNumber() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int number = -1;
		
		//WHEN
		IllegalArgumentException exception = assertThrows("Expected failure for Invalid Page Number",
				IllegalArgumentException.class, () -> controller.retrieveUsers(number, PAGE_LIMIT));
		
		//THEN
		assertEquals("Page number may not be less than 0!", exception.getMessage());
	}

	@Test
	void testRetrieveFailureForInvalidPageLimit() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int number = 1;
		int limit = 0;
		
		//WHEN
		IllegalArgumentException exception = assertThrows("Expected failure for Invalid Page Limit",
				IllegalArgumentException.class, () -> controller.retrieveUsers(number, limit));
		
		//THEN
		assertEquals("Page size limit has to be greater than 0!", exception.getMessage());
	}
	
	@Test
	void testRetrieveFirstPage() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int firstPage = 0;
		PageRequest request = PageRequest.of(firstPage, PAGE_LIMIT);
		Page<User> expectedPage = page(request, PAGE_LIMIT, entities.size());
		
		//WHEN
		Mockito.when(controller.getUserManager().getRepository().
				findAll(request)).thenReturn(expectedPage);
		
		//THEN
		ResponseEntity<Page<User>> actualResponse = controller.retrieveUsers(firstPage, PAGE_LIMIT);
		assertNotNull(actualResponse);
		log.info("Response: {}", actualResponse);
		assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
		
		Page<User> page = actualResponse.getBody();
		assertNotNull(page);
		
		log.info("Max total pages: {}", page.getTotalPages());
		log.info("This page number: {}", (page.getNumber() + 1));
		log.info("Total elements from all pages: {}", page.getTotalElements());
		log.info("Number of elements in this page: {}", page.getNumberOfElements());
		assertEquals((firstPage), page.getNumber(), "Actual Page Number: " + page.getNumber());
		assertEquals(PAGE_LIMIT, page.getSize(), "Actual Page Size: " + page.getSize());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
		
		List<User> users = page.getContent();
		assertEquals(expectedPage.getNumberOfElements(), page.getNumberOfElements());
		
		int i = expectedPage.getNumber() * PAGE_LIMIT;
		for(User actualUser : users) {
			String id = String.valueOf(++i);
			assertNotNull(actualUser);
			log.info("Retrieved user: {}", actualUser);
			assertEquals(id, actualUser.getId());
			assertTrue(StringUtils.isNotBlank(actualUser.getId()));
			User expectedUser = createExistingUser(String.valueOf(id));
			assertEquals(expectedUser, actualUser);
			assertEquals(expectedUser.hashCode(), actualUser.hashCode());
		}
	}
	
	@Test
	void testRetrieveLastPage() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int number = 3;
		PageRequest request = PageRequest.of(number, PAGE_LIMIT);
		Page<User> expectedPage = page(request, PAGE_LIMIT, entities.size());
		
		//WHEN
		Mockito.when(controller.getUserManager().getRepository().
				findAll(PageRequest.of(number, PAGE_LIMIT))).thenReturn(expectedPage);
		
		//THEN
		ResponseEntity<Page<User>> actualResponse = controller.retrieveUsers(number, PAGE_LIMIT);
		assertNotNull(actualResponse);
		log.info("Response: {}", actualResponse);
		assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
		
		Page<User> page = actualResponse.getBody();
		assertNotNull(page);
		
		log.info("Max total pages: {}", page.getTotalPages());
		log.info("This page number: {}", (page.getNumber() + 1));
		log.info("Total elements from all pages: {}", page.getTotalElements());
		log.info("Number of elements in this page: {}", page.getNumberOfElements());
		assertEquals(number, page.getNumber(), "Actual Page Number: " + page.getNumber());
		assertEquals(PAGE_LIMIT, page.getSize(), "Actual Page Size: " + page.getSize());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
		
		List<User> users = page.getContent();
		assertEquals(expectedPage.getNumberOfElements(), page.getNumberOfElements());
		
		int i = expectedPage.getNumber() * PAGE_LIMIT;
		for(User actualUser : users) {
			String id = String.valueOf(++i);
			assertNotNull(actualUser);
			log.info("Retrieved user: {}", actualUser);
			assertEquals(id, actualUser.getId());
			assertTrue(StringUtils.isNotBlank(actualUser.getId()));
			User expectedUser = createExistingUser(String.valueOf(id));
			assertEquals(expectedUser, actualUser);
			assertEquals(expectedUser.hashCode(), actualUser.hashCode());
		}
	}
	
	@Test
	void testRetrieveByBlankEmail() throws JsonParseException, JsonMappingException, IOException {
		//WHEN
		ResponseEntity<User> response = controller.retrieveByEmail(null);
		
		//THEN
		assertNotNull(response);
		log.info("Response: {}", response);
		assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
		assertNull(response.getBody());
	}
	
	@Test
	void testRetrieveByEmail() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String email = "user1@email.com";
		
		//WHEN
		Mockito.when(controller.getUserManager().getRepository().
				findByEmail(email)).thenReturn(entity(0, 1));
		ResponseEntity<User> response = controller.retrieveByEmail(email);
		
		//THEN
		assertNotNull(response);
		log.info("Response: {}", response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		User entity = response.getBody();
		assertNotNull(entity);
		assertEquals(email, entity.getEmail());
		assertNotNull(entity.getId());
		assertNotNull(entity.getName());
	}

	protected User entity(int number, int limit) throws JsonParseException, JsonMappingException, IOException {
		PageRequest request = PageRequest.of(number, limit);
		return page(request, limit, entities.size()).getContent().get(0);
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
		log.info("Then return a total of {}/{} users from {} to {}.", (to - from), size, (from + 1), to);
		return new PageImpl<>(entities.subList(from, to), page, entities.size());
	}

	protected User createExistingUser(String id) {
		User user = createNewUser(id);
		user.setId(id);
		return user;
	}
			
	protected User createNewUser(String id) {
		User user = new User();
		user.setEmail("user" + id + "@email.com");
		user.setName("User" + id);
		return user;
	}
}
