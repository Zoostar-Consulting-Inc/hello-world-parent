package net.zoostar.hw.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public abstract class AbstractMockTestHarness {
	
	protected static final int PAGE_LIMIT = 3;
	
	protected List<User> entities;
	
	@Mock
	protected UserRepository repository;

	@InjectMocks
	protected UserServiceImpl userManager;
	
	protected List<User> entities() throws JsonParseException, JsonMappingException, IOException {
		return Collections.unmodifiableList(new ObjectMapper().readValue(
				new ClassPathResource("data/users.json").getInputStream(),
				new TypeReference<List<User>>() { }));
	}

	protected User entity(int number, int limit) throws JsonParseException, JsonMappingException, IOException {
		PageRequest request = PageRequest.of(number, limit);
		return page(request, limit, entities.size()).getContent().get(0);
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

	@BeforeEach
	protected void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());

		entities = entities();
		log.info("Total number of users loaded: {}", entities.size());
	}

}
