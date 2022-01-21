package net.zoostar.hw;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.zoostar.hw.web.api.ProductApi;

class HelloWorldAppTest extends AbstractHelloWorldTestHarness {

	@Autowired
	ProductApi api;
	
	@Test
	void testMain() {
		assertThat(api).isNotNull();
		String[] args = new String[0];
		HelloWorldApp.main(args);
	}

}
