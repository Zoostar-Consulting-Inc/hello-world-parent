package net.zoostar.hw;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.zoostar.hw.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public abstract class AbstractHelloWorldTestHarness {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@MockBean
	protected ProductRepository productRepository;

	@Autowired
	protected MockMvc api;
	
	@Autowired
	protected ObjectMapper mapper;

}
