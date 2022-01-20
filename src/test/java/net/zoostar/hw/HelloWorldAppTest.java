package net.zoostar.hw;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.zoostar.hw.repository.ProductRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class HelloWorldAppTest {

	@MockBean
	private ProductRepository entityRepository;
	
	@Test
	void testMain() {
		String[] args = new String[0];
		HelloWorldApp.main(args);
	}

}
