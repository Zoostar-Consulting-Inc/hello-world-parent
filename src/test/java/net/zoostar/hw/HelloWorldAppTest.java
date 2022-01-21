package net.zoostar.hw;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.zoostar.hw.repository.ProductRepository;
import net.zoostar.hw.web.api.ProductApi;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class HelloWorldAppTest {

	@MockBean
	ProductRepository entityRepository;

	@Autowired
	ProductApi api;
	
	@Test
	void testMain() {
		assertThat(api).isNotNull();
		String[] args = new String[0];
		HelloWorldApp.main(args);
	}

}
