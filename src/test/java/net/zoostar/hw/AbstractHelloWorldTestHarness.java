package net.zoostar.hw;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.zoostar.hw.entity.Source;
import net.zoostar.hw.repository.ProductRepository;
import net.zoostar.hw.repository.SourceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Persistable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public abstract class AbstractHelloWorldTestHarness<E extends Persistable<T>, T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@MockBean
	protected ProductRepository repository;
	
	@MockBean
	protected SourceRepository sourceRepository;
	
	@Autowired
	protected MockMvc service;
	
	@MockBean
	protected RestTemplate rest;
	
	@Autowired
	protected ObjectMapper mapper;
	
	protected List<E> records;
	
	protected abstract TypeReference<List<E>> getListTypeReference();

	@BeforeEach
	public void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());

		records = Collections.unmodifiableList(mapper.readValue(
				new ClassPathResource(getResourcePath()).getInputStream(), getListTypeReference()));
		log.info("Total number of records loaded: {}", records.size());
	}

	protected abstract String getResourcePath();

	protected Source toSource(String sourceCode) {
		var source = new Source();
		source.setBaseUrl(sourceCode);
		source.setId(UUID.randomUUID().toString());
		source.setSourceCode(sourceCode);
		source.setName(sourceCode);
		return source;
	}

	protected Page<E> page(List<E> totalProducts,
			PageRequest page) throws IOException {
		int size = page.getPageSize();
		int from = page.getPageNumber() * size;
		int to = from + size;
		if(to > totalProducts.size()) {
			to = from + (totalProducts.size() - from);
		}
		log.info("Given page number {} and page size {}...", (page.getPageNumber() + 1), size);
		log.info("Then return a total of {}/{} products from {} to {}.", (to - from), size, (from + 1), to);
		return new PageImpl<>(totalProducts.subList(from, to), page, totalProducts.size());
	}
	
}
