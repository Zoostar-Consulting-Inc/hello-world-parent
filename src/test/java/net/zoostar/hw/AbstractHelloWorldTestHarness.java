package net.zoostar.hw;

import net.zoostar.hw.repository.ProductRepository;
import net.zoostar.hw.repository.SourceRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Persistable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

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
	protected MockMvc api;
	
	@MockBean
	protected RestTemplate rest;
	
	@Autowired
	protected ObjectMapper mapper;
	
}
