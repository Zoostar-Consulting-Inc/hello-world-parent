package net.zoostar.hw;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.spring.web.plugins.Docket;

@SpringBootTest
class HelloWorldContextTest {

	@Value("${env.type}")
	private String envType;
	
	@Autowired
	private Docket docket;
	
	@Autowired
	private RestTemplate api;
	
	@Test
	void testContext() {
		assertThat(envType).isEqualTo("test");
		assertThat(docket).isNotNull();
		assertThat(api).isNotNull();
	}

}
